package infrrd.ai.nevernote

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.ActionMode
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat
import java.text.Format
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import android.os.Environment.getExternalStorageDirectory
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.widget.Toast
import java.io.File


class MainActivity : BaseActivity(), NotesAdapter.ActionBarCallback {

    override var actionMode: ActionMode? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: NotesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var myDataset: MutableList<Note> = ArrayList()
    private lateinit var imageUri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        viewManager = LinearLayoutManager(this)
        myDataset = assignNotes()
        viewAdapter = NotesAdapter(this, this, myDataset)
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
            startActivityForResult(intent,2)
        }
        audio.setOnClickListener {
            val intent = Intent(this, AudioRecorder::class.java)
            startActivity(intent)}
        camera.setOnClickListener {
            takePicture()
        }

    }


    private fun takePicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photo = File(getExternalStorageDirectory(), "Pic.jpg")
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photo))
            imageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photo)
            startActivityForResult(intent, 1)
        }
        else {
            Toast.makeText(this, "Please provide camera access", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK ) {
                    val capturedImage = imageUri
                    contentResolver.notifyChange(capturedImage, null)

                    Toast.makeText(this, capturedImage.toString(), Toast.LENGTH_LONG).show()
                }
            }
            2 -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("LOOK HERE", data?.getStringExtra("result"))
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
            }
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

                val header: String = notes.get(position).created.toString().subSequence(3,7).toString()+ " "+  notes.get(position).created.toString().subSequence(30,34)
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



    var formatter = SimpleDateFormat("dd-MMMM-yyyy")
    fun assignNotes(): MutableList<Note> {

        val notes: MutableList<Note> = ArrayList()
        notes.add(Note("Task1",
                "Body1",formatter.parse("7-JUNE-2013"), false))
        notes.add(Note("Task2",
                "Body2",formatter.parse("7-JUNE-2013"), false))
        notes.add(Note("Task3",
                "Body3",formatter.parse("7-JUNE-2013"), false))
        notes.add(Note("Task4",
                "Body4",formatter.parse("7-JUNE-2013"), false))
        notes.add(Note("Task5",
                "Body5",formatter.parse("7-JUNE-2013"), false))
        notes.add(Note("Task6",
                "Body6",formatter.parse("7-JUNE-2013"), false))
        notes.add(Note("Task7",
                "Body7",formatter.parse("7-JUNE-2013"), false))
        notes.add(Note("Task7",
                "Body7",formatter.parse("7-JUNE-2013"), false))
        notes.add(Note("Task7",
                "Body7",formatter.parse("7-JUNE-2013"), false))
        notes.add(Note("Task1",
                "Body1",formatter.parse("8-AUGUST-2013"), false))
        notes.add(Note("Task2",
                "Body2",formatter.parse("8-AUGUST-2013"), false))
        notes.add(Note("Task3",
                "Body3",formatter.parse("8-AUGUST-2013"), false))
        notes.add(Note("Task4",
                "Body4",formatter.parse("8-AUGUST-2013"), false))
        notes.add(Note("Task5",
                "Body5",formatter.parse("8-AUGUST-2013"), false))
        notes.add(Note("Task6",
                "Body6",formatter.parse("8-AUGUST-2013"), false))
        notes.add(Note("Task7",
                "Body7",formatter.parse("8-AUGUST-2013"), false))
        notes.add(Note("Task7",
                "Body7",formatter.parse("8-AUGUST-2013"), false))
        notes.add(Note("Task7",
                "Body7",formatter.parse("8-AUGUST-2013"), false))

        return notes
    }
}
