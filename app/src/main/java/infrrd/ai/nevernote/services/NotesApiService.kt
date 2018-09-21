package infrrd.ai.nevernote.services

import io.reactivex.Observable
import infrrd.ai.nevernote.Note
import retrofit2.http.*


interface NotesApiService {

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

}