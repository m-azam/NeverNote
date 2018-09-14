package infrrd.ai.nevernote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
object ApiServiceGenerator {
    private val BASE_URL = "http://192.168.0.118:8080"

    private val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

    private var retrofit = builder.build()

    private val httpClient = OkHttpClient.Builder()

    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

    fun <S> createService(serviceClass: Class<S>): S {
        if(!httpClient.interceptors().contains(logging)){
            httpClient.addInterceptor(logging)
            builder.client(httpClient.build())
            retrofit = builder.build()
        }
        return retrofit.create(serviceClass)
    }
}