package infrrd.ai.nevernote

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.view.ActionMode

import android.support.v7.widget.LinearLayoutManager

import android.support.v7.widget.RecyclerView
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : BaseActivity(), MyAdapter.ActionBarCallback, MyAdapter.DisplaySelectionCallback {

    override var actionMode: ActionMode? = null

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MyAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var myDataset: MutableList<Note> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        viewManager = LinearLayoutManager(this)
        myDataset = assignNotes()
        viewAdapter = MyAdapter(this, this, this, myDataset)
        recyclerView = findViewById<RecyclerView>(R.id.note_recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }
        val sectionItemDecoration = RecyclerSectionItemDecoration(100,
                true,
                getSectionCallback(myDataset))
        recyclerView.addItemDecoration(sectionItemDecoration)

        text.setOnClickListener{

            val intent = Intent(this, NewNote::class.java)
            startActivity(intent)
        }


    }



    private fun getSectionCallback(notes: List<Note>): RecyclerSectionItemDecoration.SectionCallback {
        return object : RecyclerSectionItemDecoration.SectionCallback {
            override fun isSection(position: Int): Boolean {
                return position == 0 || getHeader(position) != getHeader(position - 1)
            }

            override fun getSectionHeader(position: Int): CharSequence {
                return getHeader(position)
            }

            fun getHeader(position:Int): String{
                val header: String = notes.get(position).created.toString().subSequence(4,7).toString()+ ", "+  notes.get(position).created.toString().subSequence(30,34)
                return header
            }
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun startActionBar() {
        actionMode = this.startActionMode(ActionBarCallBack(viewAdapter))
    }

    override fun finishActionBar() {
        actionMode?.finish()
    }

    override fun displayselection() {
        viewAdapter.multiSelect = true
        viewAdapter.notifyDataSetChanged()
    }


    //populating the data set here please ignore
    var formatter = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
    fun assignNotes(): MutableList<Note>{
        val notes: MutableList<Note> = ArrayList()
        notes.add(Note("Task1",
                "Body1",formatter.parse("7-Jun-2013"), false))
        notes.add(Note("Task2",
                "Body2",formatter.parse("7-Jun-2013"), false))
        notes.add(Note("Task3",
                "Body3",formatter.parse("7-Jun-2013"), false))
        notes.add(Note("Task4",
                "Body4",formatter.parse("7-Jun-2013"), false))
        notes.add(Note("Task5",
                "Body5",formatter.parse("7-Jun-2013"), false))
        notes.add(Note("Task6",
                "Body6",formatter.parse("7-Jun-2013"), false))
        notes.add(Note("Task7",
                "Body7",formatter.parse("7-Jun-2013"), false))
        notes.add(Note("Task7",
                "Body7",formatter.parse("7-Jun-2013"), false))
        notes.add(Note("Task7",
                "Body7",formatter.parse("7-Jun-2013"), false))
        notes.add(Note("Task1",
                "Body1",formatter.parse("8-Aug-2013"), false))
        notes.add(Note("Task2",
                "Body2",formatter.parse("8-Aug-2013"), false))
        notes.add(Note("Task3",
                "Body3",formatter.parse("8-Aug-2013"), false))
        notes.add(Note("Task4",
                "Body4",formatter.parse("8-Aug-2013"), false))
        notes.add(Note("Task5",
                "Body5",formatter.parse("8-Aug-2013"), false))
        notes.add(Note("Task6",
                "Body6",formatter.parse("8-Aug-2013"), false))
        notes.add(Note("Task7",
                "Body7",formatter.parse("8-Aug-2013"), false))
        notes.add(Note("Task7",
                "Body7",formatter.parse("8-Aug-2013"), false))
        notes.add(Note("Task7",
                "Body7",formatter.parse("8-Aug-2013"), false))

        return notes
    }
}
