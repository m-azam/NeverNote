package infrrd.ai.nevernote

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ActionMode
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.base_activity.*

class Trash : BaseActivity(),NotesAdapter.ActionBarCallback,
        ActionBarCallBack.OnDeleteSelectionListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: NotesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var trashDataset: MutableList<Note> = ArrayList()
    override var actionMode: ActionMode? = null

    override fun onStart() {
        super.onStart()
        nav_view.menu.getItem(1).setChecked(true)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewManager = LinearLayoutManager(this)
        viewAdapter = NotesAdapter(null,this, this, trashDataset)
        recyclerView = findViewById<RecyclerView>(R.id.trash_recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val sectionItemDecoration = RecyclerSectionItemDecoration(100,
                true,
                getSectionCallback(trashDataset))
        recyclerView.addItemDecoration(sectionItemDecoration)
        nav_view.menu.getItem(1).setChecked(true)
        loadTasks()
    }

    override fun getContentView(): Int {
        return R.layout.activity_trash
    }

    private fun getSectionCallback(notes: List<Note>): RecyclerSectionItemDecoration.SectionCallback {
        return object : RecyclerSectionItemDecoration.SectionCallback {
            override fun isSection(position: Int): Boolean {
                return position == 0 || getHeader(position) != getHeader(position - 1)
            }

            override fun getSectionHeader(position: Int): CharSequence {
                return getHeader(position)
            }

            fun getHeader(position:Int): String {
                val header: String = notes.get(position).created.toString().subSequence(3,7).toString()+ " "+  notes.get(position).created.toString().subSequence(30,34)
                return header
            }
        }
    }

    override fun startActionBar() {
        actionMode = this.startActionMode(ActionBarCallBack(viewAdapter,this))
    }

    override fun finishActionBar() {
        actionMode?.finish()
    }

    override fun onDeleteSelection() {
        var deleteList:MutableList<Int>  = ArrayList()
        viewAdapter.selectedArray.sort()
        viewAdapter.selectedArray.reverse()

        for(index in viewAdapter.selectedArray) {
            deleteList.add(trashDataset[index].id)
        }
        viewAdapter.selectedArray.clear()
        finishActionBar()
        deleteTasks(deleteList)
        viewAdapter.selectCount = 0
        viewAdapter.multiSelect = false
    }

    private fun deleteTasks(deleteList:List<Int>) {
        apiService.delete(deleteList.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Toast.makeText(this,"Notes Deleted", Toast.LENGTH_LONG).show()
                            loadTasks()

                        },
                        { error -> Log.d("ERROR", error.message) }
                )

    }

    private var disposable: Disposable? = null

    private val apiService by lazy {
        ApiService.create()
    }


    private fun loadTasks() {

        disposable = apiService.getTrash()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            for(note in result) {
                                note.selected = false
                            }

                            trashDataset.clear()
                            trashDataset.addAll(result)
                            viewAdapter.notifyDataSetChanged()
                        },
                        { error -> Log.d("ERROR", error.message) }
                )
    }

}
