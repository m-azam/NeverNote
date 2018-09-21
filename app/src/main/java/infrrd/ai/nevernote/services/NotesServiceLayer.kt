package infrrd.ai.nevernote.services

import android.util.Log
import infrrd.ai.nevernote.Note
import infrrd.ai.nevernote.objects.NotesServiceGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class NotesServiceLayer {

    private var disposable: Disposable? = null

    private val apiService by lazy {
        NotesServiceGenerator.create()
    }
    fun loadNotes(passData: (MutableList<Note>) -> Unit) {

        disposable = apiService.getAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            passData(result)
                        },
                        { error -> Log.d("ERROR", error.message) }
                )
    }

    fun moveToTrash(passData: (String) -> Unit,deleteList:String) {

        disposable = apiService.trash(deleteList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            passData(result)
                        },
                        { error -> Log.d("ERROR", error.message) }
                )
    }

    fun addNewNote(addSuccess: (String) -> Unit,addFailure:(String) -> Unit,note:Note) {
        disposable = apiService.addNewNote(note.title,note.body,
                note.selected,note.created,note.latitude,note.longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            addSuccess(result)
                        },
                        { error ->
                            addFailure(error.message.toString())

                        }
                )
    }

    fun loadTrash(passData: (MutableList<Note>) -> Unit) {

        disposable = apiService.getTrash()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            passData(result)
                        },
                        { error -> Log.d("ERROR", error.message) }
                )

    }

    fun deleteNote(passData: (String) -> Unit,deleteList:String) {

        disposable = apiService.delete(deleteList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            passData(result)
                        },
                        { error -> Log.d("ERROR", error.message) }
                )
    }
}