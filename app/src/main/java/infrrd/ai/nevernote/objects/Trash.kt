package infrrd.ai.nevernote.objects

import android.os.Bundle
import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ActionMode
import infrrd.ai.nevernote.*

import kotlinx.android.synthetic.main.activity_trash.*

class Trash : BaseActivity(),NotesAdapter.ActionBarCallback,
        ActionBarCallBack.OnDeleteSelectionListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: NotesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var trashDataset: MutableList<Note> = ArrayList()
    override var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewManager = LinearLayoutManager(this)
        viewAdapter = NotesAdapter(null,this, this, trashDataset)
        recyclerView = findViewById<RecyclerView>(R.id.note_recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val sectionItemDecoration = RecyclerSectionItemDecoration(100,
                true,
                getSectionCallback(trashDataset))
        recyclerView.addItemDecoration(sectionItemDecoration)
        setContentView(R.layout.activity_trash)
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
        viewAdapter.selectedArray.sort()
        viewAdapter.selectedArray.reverse()
        for(index in viewAdapter.selectedArray) {
            trashDataset.removeAt(index)
        }
        viewAdapter.selectedArray.clear()
        viewAdapter.notifyDataSetChanged()
        finishActionBar()
        viewAdapter.selectCount = 0
        viewAdapter.multiSelect = false

    }

}
