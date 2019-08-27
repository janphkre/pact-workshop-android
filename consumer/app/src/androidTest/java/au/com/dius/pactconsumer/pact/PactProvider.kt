package au.com.dius.pactconsumer.pact

import androidx.test.espresso.IdlingRegistry
import au.com.dius.pact.external.StatefullPactWebServer
import au.com.dius.pact.model.RequestResponsePact
import au.com.dius.pactconsumer.apiclient.AnimalsProvider
import org.junit.Assert
import java.lang.IllegalStateException

object PactProvider {

    // You may want to select a error code that is not in the range of your expected http codes
    private const val pactErrorCode = 999

    private var pactServer: StatefullPactWebServer? = null
    private var dataProvider: PactAnimalProvider? = null

    fun setupServer() {
        pactServer = StatefullPactWebServer(false, pactErrorCode). also {
            dataProvider = PactAnimalProvider(it.getUrlString())
        }
        IdlingRegistry.getInstance().register(dataProvider?.idlingResource)
    }

    fun teardownServer() {
        IdlingRegistry.getInstance().unregister(dataProvider?.idlingResource)
        dataProvider = null
        Assert.assertTrue("Not all pacts have been invoked!", pactServer?.validatePactsCompleted() != false)
        pactServer?.teardown()
        pactServer = null
    }

    fun getDataProvider(): AnimalsProvider {
        return dataProvider ?: throw IllegalStateException("The pact provider has not been setup yet!")
    }

    fun setPact(vararg pacts: RequestResponsePact) {
        pactServer?.let {
            it.clearPacts()
            it.addPacts(pacts.asList())
        } ?: throw IllegalStateException("The pact provider has not been setup yet!")
    }
}