package infrrd.ai.nevernote

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ActionMode
import android.widget.Toast
import infrrd.ai.nevernote.services.NotesServiceLayer
import kotlinx.android.synthetic.main.base_activity.*

class Trash : BaseActivity(),NotesAdapter.ActionBarCallback,
        ActionBarCallBack.OnDeleteSelectionListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: NotesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var trashDataset: MutableList<Note> = ArrayList()
    private var serviceLayer: NotesServiceLayer = NotesServiceLayer()

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
        loadNotes()
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
        var deleteIds:MutableList<Int> = ArrayList()
        for(index in viewAdapter.selectedArray) {
            deleteIds.add(viewAdapter.filteredNotes[index].id)
        }
        deleteNotes(deleteIds)
    }

    private fun deleteNotes(deleteList:List<Int>) {
        serviceLayer.deleteNote({
                            Toast.makeText(this,"Notes Deleted", Toast.LENGTH_LONG).show()
                            finishActionBar()
                            loadNotes()
                        },deleteList.toString())
    }


    private fun loadNotes() {
        serviceLayer.loadTrash{
            for(note in it) {
                                note.selected = false
                            }
                            trashDataset.clear()
                            trashDataset.addAll(it)
                            viewAdapter.notifyDataSetChanged()
                        }
    }
}
