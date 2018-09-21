package infrrd.ai.nevernote.objects

import com.google.gson.GsonBuilder
import infrrd.ai.nevernote.services.NotesApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NotesServiceGenerator {
    fun create(): NotesApiService {
        val httpClient = OkHttpClient.Builder()
        httpClient.interceptors().clear()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                    .header("Authorization", "token")
                    .build()
            chain.proceed(request)
        }
        var gson = GsonBuilder()
                .setLenient()
                .create()
        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .baseUrl("http://192.168.0.118:8080/")
                .build()

        return retrofit.create(NotesApiService::class.java)
    }
}