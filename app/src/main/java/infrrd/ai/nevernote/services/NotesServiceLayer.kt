package infrrd.ai.nevernote.services

import android.util.Log
import infrrd.ai.nevernote.Note
import infrrd.ai.nevernote.objects.NotesServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotesServiceLayer {
    val service = NotesServiceGenerator.createService(NotesServiceRetroFit::class.java)
    fun getallnotes(passData: (MutableList<Note>) -> Unit) {
        val callAsync: Call<MutableList<Note>> = service.getAllNotes()
        callAsync.enqueue(object: Callback<MutableList<Note>> {
            override fun onResponse(call: Call<MutableList<Note>>, response: Response<MutableList<Note>>) {

              if(response.isSuccessful) {
                  passData(response.body()?.let { it }?: arrayListOf())
              } else {
              }
            }
            override fun onFailure(call: Call<MutableList<Note>>, throwable: Throwable) {
                Log.d("Call", throwable.toString())
            }
        })
    }
}