package infrrd.ai.nevernote

import retrofit2.Call
import retrofit2.http.*

interface Api {

    @GET("/getallnote")
    fun getAllNote(): Call<List<NoteApiModel>>

    @POST("/test")
    fun verfiyUser(@Body note:NoteApiModel): Call<NoteApiModel>
}