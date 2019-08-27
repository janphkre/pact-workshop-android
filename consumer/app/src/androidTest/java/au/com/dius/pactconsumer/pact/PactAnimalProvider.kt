package au.com.dius.pactconsumer.pact

import androidx.test.espresso.idling.CountingIdlingResource
import au.com.dius.pactconsumer.apiclient.AnimalsProvider
import au.com.dius.pactconsumer.model.AnimalCollection
import rx.Observable
import rx.internal.operators.OperatorDoOnUnsubscribe
import java.util.*

class PactAnimalProvider(urlString: String): AnimalsProvider {

    val idlingResource = CountingIdlingResource("PactAnimalProviderIdlingResource")
    private val animalsProvider = AnimalsProvider.getInstance(urlString)

    override fun getAnimals(validDate: Calendar): Observable<AnimalCollection> {
        idlingResource.increment()
        return animalsProvider.getAnimals(validDate).lift(OperatorDoOnUnsubscribe {
            idlingResource.decrement()
        })
    }
}