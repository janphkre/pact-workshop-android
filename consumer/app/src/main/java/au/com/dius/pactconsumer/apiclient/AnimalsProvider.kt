package au.com.dius.pactconsumer.apiclient

import au.com.dius.pactconsumer.model.AnimalCollection
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable
import java.util.*

interface AnimalsProvider {

    @GET("api/v1/animals")
    fun getAnimals(@Query("valid_date") validDate: Calendar): Observable<AnimalCollection>

    companion object {
        fun getInstance(baseUrl: String): AnimalsProvider {
            val gson = Gson()
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()
            return retrofit.create(AnimalsProvider::class.java)
        }
    }
}