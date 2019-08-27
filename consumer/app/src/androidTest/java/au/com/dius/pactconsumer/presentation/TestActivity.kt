package au.com.dius.pactconsumer.presentation

import au.com.dius.pactconsumer.apiclient.AnimalsProvider
import au.com.dius.pactconsumer.pact.PactProvider

class TestActivity: MainActivity() {

    override fun getDataProvider(): AnimalsProvider {
        return PactProvider.getDataProvider()
    }
}