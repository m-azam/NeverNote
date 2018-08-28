package infrrd.ai.nevernote

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager

import android.support.v7.widget.RecyclerView
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity()  {
    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MyAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var myDataset: MutableList<Note> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewManager = LinearLayoutManager(this)
        myDataset = assignNotes()
        viewAdapter = MyAdapter(myDataset)
        recyclerView = findViewById<RecyclerView>(R.id.note_recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }
        val sectionItemDecoration = RecyclerSectionItemDecoration(100,
                true,
                getSectionCallback(myDataset))
        recyclerView.addItemDecoration(sectionItemDecoration)


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

    var formatter = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
    fun assignNotes(): MutableList<Note>{
        val notes: MutableList<Note> = ArrayList()
        notes.add(Note("Task1",
                "Body1",formatter.parse("7-Jun-2013")))
        Log.d("LookHere",notes[0].created.toString())
        notes.add(Note("Task2",
                "Body2",formatter.parse("7-Jun-2013")))
        notes.add(Note("Task3",
                "Body3",formatter.parse("7-Jun-2013")))
        notes.add(Note("Task4",
                "Body4",formatter.parse("7-Jun-2013")))
        notes.add(Note("Task5",
                "Body5",formatter.parse("7-Jun-2013")))
        notes.add(Note("Task6",
                "Body6",formatter.parse("7-Jun-2013")))
        notes.add(Note("Task7",
                "Body7",formatter.parse("7-Jun-2013")))
        notes.add(Note("Task7",
                "Body7",formatter.parse("7-Jun-2013")))
        notes.add(Note("Task7",
                "Body7",formatter.parse("7-Jun-2013")))
        notes.add(Note("Task1",
                "Body1",formatter.parse("8-Aug-2013")))
        Log.d("LookHere",notes[7].created.toString())
        notes.add(Note("Task2",
                "Body2",formatter.parse("8-Aug-2013")))
        notes.add(Note("Task3",
                "Body3",formatter.parse("8-Aug-2013")))
        notes.add(Note("Task4",
                "Body4",formatter.parse("8-Aug-2013")))
        notes.add(Note("Task5",
                "Body5",formatter.parse("8-Aug-2013")))
        notes.add(Note("Task6",
                "Body6",formatter.parse("8-Aug-2013")))
        notes.add(Note("Task7",
                "Body7",formatter.parse("8-Aug-2013")))
        notes.add(Note("Task7",
                "Body7",formatter.parse("8-Aug-2013")))
        notes.add(Note("Task7",
                "Body7",formatter.parse("8-Aug-2013")))

        return notes
    }


}
