package au.com.dius.pactconsumer.pact

import au.com.dius.pact.consumer.ConsumerPactBuilder
import org.apache.http.entity.ContentType

object AnimalPacts {

    private val consumerName = "app_android"
    private val providerName = "animal_service"

    val animalCollectionPact by lazy {
        ConsumerPactBuilder(consumerName)
            .hasPactWith(providerName)
            .uponReceiving("GET Animals")
            .path("api/v1/animals")
            .method("GET")
            .matchHeader("Accept-Language", "[a-z]{2}", "en")
            .matchQuery("valid_date", "(19[0-9]{2}|[2-9][0-9]{3})-((0(1|3|5|7|8)|10|12)-" +
                    "(0[1-9]|1[0-9]|2[0-9]|3[0-1])|(0(4|6|9)|11)-(0[1-9]|1[0-9]|2[0-9]|30)|(02)-(0[1-9]|1[0-9]|2[0-9]))T" +
                    "(0[0-9]|1[0-9]|2[0-3])(:[0-5][0-9]){2}(Z|((\\+?|-)(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]))")
            .willRespondWith()
            .status(200)
            .headers(mapOf(Pair("Content-Type", ContentType.APPLICATION_JSON.toString())))
            .body()
    }
}