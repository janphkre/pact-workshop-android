package au.com.dius.pactconsumer.pact

import au.com.dius.pact.consumer.ConsumerPactBuilder
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.model.RequestResponsePact
import org.apache.http.entity.ContentType

object AnimalPacts {

    private val consumerName = "app_android"
    private val providerName = "animal_service"
    private val dateRegex = "(19[0-9]{2}|[2-9][0-9]{3})-((0(1|3|5|7|8)|10|12)-" +
    "(0[1-9]|1[0-9]|2[0-9]|3[0-1])|(0(4|6|9)|11)-(0[1-9]|1[0-9]|2[0-9]|30)|(02)-(0[1-9]|1[0-9]|2[0-9]))T" +
    "(0[0-9]|1[0-9]|2[0-3])(:[0-5][0-9]){2}(Z|((\\+?|-)(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]))"

    val animalCollectionPact by lazy {
        ConsumerPactBuilder(consumerName)
            .hasPactWith(providerName)
            .uponReceiving("GET Animals")
            .path("api/v1/animals")
            .method("GET")
            .matchHeader("Accept-Language", "[a-z]{2}", "en")
            .matchQuery("valid_date", dateRegex)
            .willRespondWith()
            .status(200)
            .headers(mapOf(Pair("Content-Type", ContentType.APPLICATION_JSON.toString())))
            .body(PactDslJsonBody()
                .stringMatcher("valid_date", dateRegex, "2019-09-02T15:23:32+00:00")
                .array("animals")
                    .`object`()
                    .stringMatcher("name", ".*", "doggo")
                    .stringMatcher("image", "bird|cat|dog", "dog")
                    .closeObject()
                ?.closeArray()
                ?.close())
            .toPact()
    }
}