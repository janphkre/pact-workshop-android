package au.com.dius.pactconsumer.util

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Handler
import android.os.Looper
import android.os.Message
import rx.Scheduler
import rx.Subscription
import rx.exceptions.OnErrorNotImplementedException
import rx.functions.Action0
import rx.plugins.RxJavaHooks
import rx.subscriptions.Subscriptions
import java.util.concurrent.TimeUnit

internal class LooperScheduler : Scheduler {
    private val handler: Handler

    constructor(looper: Looper) {
        handler = Handler(looper)
    }

    constructor(handler: Handler) {
        this.handler = handler
    }

    override fun createWorker(): Scheduler.Worker {
        return HandlerWorker(handler)
    }

    internal class HandlerWorker(private val handler: Handler) : Scheduler.Worker() {
        @Volatile
        private var unsubscribed: Boolean = false

        override fun unsubscribe() {
            unsubscribed = true
            handler.removeCallbacksAndMessages(this /* token */)
        }

        override fun isUnsubscribed(): Boolean {
            return unsubscribed
        }

        override fun schedule(action: Action0, delayTime: Long, unit: TimeUnit): Subscription {
            if (unsubscribed) {
                return Subscriptions.unsubscribed()
            }

            val scheduledAction =
                ScheduledAction(action, handler)

            val message = Message.obtain(handler, scheduledAction)
            message.obj = this // Used as token for unsubscription operation.

            handler.sendMessageDelayed(message, unit.toMillis(delayTime))

            if (unsubscribed) {
                handler.removeCallbacks(scheduledAction)
                return Subscriptions.unsubscribed()
            }

            return scheduledAction
        }

        override fun schedule(action: Action0): Subscription {
            return schedule(action, 0, TimeUnit.MILLISECONDS)
        }
    }

    internal class ScheduledAction(private val action: Action0, private val handler: Handler) : Runnable, Subscription {
        @Volatile
        private var unsubscribed: Boolean = false

        override fun run() {
            try {
                action.call()
            } catch (e: Throwable) {
                // nothing to do but print a System error as this is fatal and there is nowhere else to throw this
                val ie = if (e is OnErrorNotImplementedException) {
                    IllegalStateException("Exception thrown on Scheduler.Worker thread. Add `onError` handling.", e)
                } else {
                    IllegalStateException("Fatal Exception thrown on Scheduler.Worker thread.", e)
                }
                RxJavaHooks.getOnError().call(ie)
                val thread = Thread.currentThread()
                thread.uncaughtExceptionHandler.uncaughtException(thread, ie)
            }
        }

        override fun unsubscribe() {
            unsubscribed = true
            handler.removeCallbacks(this)
        }

        override fun isUnsubscribed(): Boolean {
            return unsubscribed
        }
    }
}