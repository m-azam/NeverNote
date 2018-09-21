package infrrd.ai.nevernote.services

import infrrd.ai.nevernote.Note
import retrofit2.Call
import retrofit2.http.GET

interface NotesServiceRetroFit {
    @GET("/getallnote")
    fun getAllNotes(): Call<MutableList<Note>>
}