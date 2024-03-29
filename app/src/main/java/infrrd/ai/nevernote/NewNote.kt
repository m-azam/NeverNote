package infrrd.ai.nevernote

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import com.google.gson.Gson
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import infrrd.ai.nevernote.services.NotesServiceLayer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.new_note.*
import java.util.*


class NewNote: AppCompatActivity() {
    private val TAG = NewNote::class.java.simpleName
    var textOptionsVisible: Boolean = false
    lateinit var menu:Menu
    var isBold: Boolean = false
    var isItalic: Boolean = false
    var isUnderline: Boolean = false
    var isStrike: Boolean = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var latitiude:Double? = null
    private var longitude:Double? = null
    private val MY_PERMISSION_REQUEST_LOCATION = 1
    private var position:Int = -1
    private var serviceLayer: NotesServiceLayer = NotesServiceLayer()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_note)
        setSupportActionBar(new_note_toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        note_body.setEditorBackgroundColor(Color.TRANSPARENT)
        note_body.setEditorFontSize(18)
        note_body.setEditorFontColor(Color.BLACK)
        initTextFormatListners()

        getLocation()

        val intent: Intent = getIntent()
        position = intent.getIntExtra("Position",-1)

        note_body.setOnTextChangeListener {
            if(it.isEmpty()) {
                note_body_hint.visibility  = View.VISIBLE
                note_body.clearHistory()

            }
            else {
                note_body_hint.visibility  = View.GONE
            }
        }



        note_body_hint.setOnClickListener {
            note_body.focusEditor()
        }

        note_body.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                textOptionsVisible = false
                text_format_options.visibility = View.GONE

            }
        }
        if(position != -1) {
            note_body.html = intent.getStringExtra("Body")
            if(!checkNull(intent.getStringExtra("Body"))) {
                note_body_hint.visibility = View.GONE
            }
            note_title.setText(intent.getStringExtra("Title"))
        }
        note_body.setTextBackgroundColor(R.color.theme_light)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.new_note_menu, menu)
        this.menu = menu
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.save -> {
                var title : String
                var body: String
                if(checkNull(note_title.text.toString())&&checkNull(note_body.html)) {
                    Toast.makeText(this,"Cannot save empty note",Toast.LENGTH_SHORT).show()
                }

                else {
                    if(note_title.text.toString().isEmpty()) {
                        note_title.setText(R.string.untitled_note)
                        title = "Untitled Note"
                    }
                    else {
                        title = note_title.text.toString()
                    }
                    if(checkNull(note_body.html)) {
                        body = ""
                    }
                    else {
                        body = note_body.html
                    }
                    var newNote = Note(0,title,body,Date(System.currentTimeMillis()).toString(), false, latitiude, longitude)
                    addTask(newNote)


                }
            }
            R.id.redo_text -> {
                note_body.redo()

            }
            R.id.undo_text -> {
                note_body.undo()
            }

            R.id.text_format -> {
                if(textOptionsVisible) {
                    textOptionsVisible = false
                    text_format_options.visibility = View.GONE
                    item.setIcon(R.drawable.text_format_icon_deselected)

                }
                else if(note_body.isFocused) {
                    textOptionsVisible = true
                    text_format_options.visibility = View.VISIBLE
                    item.setIcon(R.drawable.text_format_icon)
                }
            }
            else -> super.onOptionsItemSelected(item)
        }

        return super.onOptionsItemSelected(item)
    }


    private fun addTask(note:Note) {

        progress_bar.visibility = View.VISIBLE
        serviceLayer.addNewNote({progress_bar.visibility = View.GONE
            if(it.equals("true")) {
                setResult(Activity.RESULT_OK)
            }
            else {
                setResult(Activity.RESULT_CANCELED)
            }
            finish()},{setResult(Activity.RESULT_CANCELED)
            finish()},note)
    }

    fun initTextFormatListners() {

        onClickBold()

        onClickItalic()

        onClickUnderline()

        onClickStrike()

        onClickHighlight()

        action_indent.setOnClickListener {
            note_body.setIndent()
        }

        action_outdent.setOnClickListener {
            note_body.setOutdent()
        }

        action_align_left.setOnClickListener {
            note_body.setAlignLeft()
        }

        action_align_right.setOnClickListener {
            note_body.setAlignRight()
        }

        action_align_center.setOnClickListener {
            note_body.setAlignCenter()
        }

        action_insert_numbers.setOnClickListener {
            note_body.setNumbers()
        }

        action_insert_bullets.setOnClickListener {
            note_body.setBullets()
        }

        action_insert_checkbox.setOnClickListener {
            note_body.insertTodo()
        }
    }


    fun onClickBold() {

        action_bold.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if(isBold) {
                    (view as ImageButton).setImageResource(R.drawable.bold)
                }
                else {
                    (view as ImageButton).setImageResource(R.drawable.bold_selected)
                }
                note_body.setBold()
                isBold = !isBold
            }
        })
    }

    fun onClickItalic() {

        action_italic.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if(isItalic) {
                    (view as ImageButton).setImageResource(R.drawable.italic)

                }
                else {
                    (view as ImageButton).setImageResource(R.drawable.italic_selected)

                }
                note_body.setItalic()
                isItalic = !isItalic
            }
        })
    }

    fun onClickUnderline() {

        action_strikethrough.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if(isStrike) {
                    (view as ImageButton).setImageResource(R.drawable.strike)

                }
                else {
                    (view as ImageButton).setImageResource(R.drawable.strike_selected)

                }
                note_body.setStrikeThrough()
                isStrike = !isStrike
            }
        })
    }

    fun onClickStrike() {

        action_underline.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if(isUnderline) {
                    (view as ImageButton).setImageResource(R.drawable.underline)

                }
                else {
                    (view as ImageButton).setImageResource(R.drawable.underline_selected)
                }
                note_body.setUnderline()
                isUnderline = !isUnderline
            }
        })
    }

    fun onClickHighlight() {
        action_highlight.setOnClickListener(object : View.OnClickListener {
            private var isChanged: Boolean = false
            override fun onClick(view: View) {
                if(isChanged) {
                    note_body.setTextBackgroundColor(Color.TRANSPARENT)
                }
                else {
                    note_body.setTextBackgroundColor(Color.YELLOW)
                }
                isChanged = !isChanged
            }
        })
    }

    fun getLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                requestLocationAccess()
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location : Location? ->
                    latitiude = location?.latitude
                    longitude = location?.longitude
                }
            }
            else{
                requestLocationAccess()
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location : Location? ->
                    latitiude = location?.latitude
                    longitude = location?.longitude
                }
            }
        }
        else{
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location : Location? ->
                latitiude = location?.latitude
                longitude = location?.longitude
            }
        }
    }

    fun requestLocationAccess(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), MY_PERMISSION_REQUEST_LOCATION)
    }
}

