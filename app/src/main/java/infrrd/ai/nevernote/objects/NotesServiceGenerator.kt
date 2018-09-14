package infrrd.ai.nevernote.objects

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NotesServiceGenerator {
    private const val baseUrl = "http://192.168.0.118:8080"
    val builder: Retrofit.Builder = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
    val retrofit = builder.build()
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}