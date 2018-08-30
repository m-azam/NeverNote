package infrrd.ai.nevernote

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import jp.wasabeef.richeditor.RichEditor
import kotlinx.android.synthetic.main.new_note.*
import kotlinx.android.synthetic.main.new_note.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class NewNote : AppCompatActivity() {
    private val TAG = NewNote::class.java.simpleName
    var textOptionsVisible: Boolean = false
    lateinit var menu:Menu
    var isBold: Boolean = false
    var isItalic: Boolean = false
    var isUnderline: Boolean = false
    var isStrike: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_note)
        setSupportActionBar(new_note_toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        note_body.setEditorBackgroundColor(Color.TRANSPARENT)
        note_body.setEditorFontSize(18)
        note_body.setEditorFontColor(Color.BLACK)
        initTextFormatListners()

        note_body_hint.setOnClickListener {
            Log.d(TAG,"inside note_body hint")
            note_body.focusEditor()
        }
        note_body.setOnTextChangeListener {
            if(it.isEmpty()) {
                note_body_hint.visibility  = View.VISIBLE
                note_body.clearHistory()

            }
            else {
                note_body_hint.visibility  = View.GONE
            }
//            if(it.length>4 && it.subSequence(it.length-2,it.length) == "b>" && !isBold) {
//                action_bold.setImageResource(R.drawable.bold_selected)
//                isBold = !isBold
//            }
        }
        note_body.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                textOptionsVisible = false
                text_format_options.visibility = View.GONE

            }
        }
        note_body.setTextBackgroundColor(R.color.theme_light)
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.new_note_menu, menu)
        this.menu = menu
        return true
    }


    @SuppressLint("SimpleDateFormat")
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.save -> {
                var title : String
                var body: String
                if(note_title.text.toString().isEmpty() && note_body.toString().isEmpty()){
                    Toast.makeText(this,"Cannot save empty note",Toast.LENGTH_SHORT).show()

                }
                if(note_title.text.toString().isEmpty())
                {
                    note_title.setText(R.string.untitled_note)
                    title = "Untitled Note"
                }
                else {
                    title = note_title.text.toString()
                }
                Toast.makeText(this,"Note Saved",Toast.LENGTH_LONG).show()

                body = note_body.html
                var newNote = Note(title,body,Date(System.currentTimeMillis()),false)
                Log.d(TAG, body)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.redo_text -> {
                note_body.redo()
                true
            }
            R.id.undo_text -> {
                note_body.undo()
                true
            }
            R.id.text_format -> {
                if(textOptionsVisible) {
                    textOptionsVisible = false
                    text_format_options.visibility = View.GONE
                    item.setIcon(R.drawable.text_format_icon_deselected)

                }
                else if(note_body.isFocused){
                    textOptionsVisible = true
                    text_format_options.visibility = View.VISIBLE
                    item.setIcon(R.drawable.text_format_icon)
                }

            }
            else -> super.onOptionsItemSelected(item)
        }
        var dateFormat: DateFormat = SimpleDateFormat("yyyy/MMM/dd")
        var date =  Date()
        return super.onOptionsItemSelected(item)

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
    fun onClickStrike(){

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
}