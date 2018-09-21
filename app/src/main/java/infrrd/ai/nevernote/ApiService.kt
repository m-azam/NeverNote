package infrrd.ai.nevernote

import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import android.text.method.TextKeyListener.clear
import com.google.gson.GsonBuilder
import retrofit2.http.*


interface ApiService {

    @GET("getallnote")
    fun getAllNotes(): Observable<MutableList<Note>>

    @GET("getTrash")
    fun getTrash(): Observable<MutableList<Note>>


    @FormUrlEncoded
    @POST("newnote")
    fun addNewNote(@Field("title") title: String,
                   @Field("body") body: String,
                   @Field("selected") selected: Boolean,
                   @Field("created") created: String,
                   @Field("latitude") latitude: Double?,
                   @Field("longitude") longitude: Double?):Observable<String>

    @FormUrlEncoded
    @POST("delete")
    fun delete(@Field("delete") deleteList:String):Observable<String>

    @FormUrlEncoded
    @POST("movetrash")
    fun trash(@Field("trash") deleteList:String):Observable<String>

    companion object {
        fun create(): ApiService {
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

            return retrofit.create(ApiService::class.java)
        }
    }
}