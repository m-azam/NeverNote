package infrrd.ai.nevernote

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.ActionMode
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.os.Environment.getExternalStorageDirectory
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.Menu
import android.support.v7.widget.SearchView
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.base_activity.*
import java.io.File

class MainActivity : BaseActivity(), NotesAdapter.ActionBarCallback, SearchView.OnQueryTextListener,
         ActionBarCallBack.OnDeleteSelectionListener {

    override var actionMode: ActionMode? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: NotesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var notesDataset: MutableList<Note> = ArrayList()
    private lateinit var imageUri: Uri
    private val permissions = arrayOf("android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE")

    val editNote = { position:Int, note:Note ->
        val intent = Intent(this, NewNote::class.java)
        intent.putExtra("Title",note.title)
        intent.putExtra("Body",note.body)
        intent.putExtra("Position",position)
        startActivityForResult(intent,2)
    }

    override fun onStart() {
        super.onStart()
        nav_view.menu.getItem(0).setChecked(true)

    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewManager = LinearLayoutManager(this)
        viewAdapter = NotesAdapter(editNote,this, this, notesDataset)
        recyclerView = findViewById<RecyclerView>(R.id.note_recycler).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }

        val sectionItemDecoration = RecyclerSectionItemDecoration(100,
                true,
                getSectionCallback(notesDataset))
        recyclerView.addItemDecoration(sectionItemDecoration)

        text.setOnClickListener {
            note_actions.collapse()
            val intent = Intent(this, NewNote::class.java)
            startActivityForResult(intent,2)
        }
        audio.setOnClickListener {
            note_actions.collapse()
            val intent = Intent(this, AudioRecorder::class.java)
            startActivity(intent)
        }
        camera.setOnClickListener {
            note_actions.collapse()
            takePicture()
        }
        loadTasks()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val searchMenuItem = menu?.findItem(R.id.action_search)
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView?
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView?.setOnQueryTextListener(this)
        searchMenuItem?.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem?): Boolean {
                toggle.isDrawerIndicatorEnabled = true //Fixes visual glitch when expanding search bar
                return true
            }
        })
        return true
    }

    private fun takePicture() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])
                || ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[1])) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", this.packageName, null)
            intent.data = uri
            startActivityForResult(intent, 1)
        }
        else {
            if (checkPermissions()) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val photo = File(getExternalStorageDirectory(), "Pic.jpg")
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(this,
                                BuildConfig.APPLICATION_ID + ".provider", photo))
                imageUri = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider", photo)
                startActivityForResult(intent, 1)
            } else {
                getPermission()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun getPermission() {
        ActivityCompat.requestPermissions(this, permissions, 1)
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
                    loadTasks()
                    Toast.makeText(this,"Note Saved",Toast.LENGTH_LONG).show()
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(this,"Note could not be saved",Toast.LENGTH_LONG).show()
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

            fun getHeader(position:Int): String {
                val header: String = notes.get(position).created.subSequence(3,7).toString()+ " "+  notes.get(position).created.toString().subSequence(30,34)
                return header
            }
        }
    }

    override fun onQueryTextChange(searchQuery: String): Boolean {
        viewAdapter.filter.filter(searchQuery)
        return true
    }

    override fun onQueryTextSubmit(searchQuery: String): Boolean {
        viewAdapter.filter.filter(searchQuery)
        Log.d("Adapter", searchQuery)
        return true
    }

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun startActionBar() {
        actionMode = this.startActionMode(ActionBarCallBack(viewAdapter,this))
        note_actions.visibility = View.GONE
    }

    override fun finishActionBar() {
        actionMode?.finish()
        note_actions.visibility = View.VISIBLE
    }

    override fun onDeleteSelection() {
        deleteTasks(viewAdapter.selectedArray)
        viewAdapter.selectCount = 0
        viewAdapter.multiSelect = false
    }

    private var disposable: Disposable? = null

    private val apiService by lazy {
        ApiService.create()
    }

    private fun loadTasks() {

        disposable = apiService.getAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            notesDataset.clear()
                            notesDataset.addAll(result)
                            viewAdapter.notifyDataSetChanged()
                        },
                        { error -> Log.d("ERROR", error.message) }
                )
    }
    private fun deleteTasks(deleteList:List<Int>) {
        apiService.trash(deleteList.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Toast.makeText(this,"Items moved to Trash",Toast.LENGTH_LONG).show()
                            finishActionBar()
                            loadTasks()
                        },
                        { error -> Log.d("ERROR", error.message) }
                )

    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}
