package infrrd.ai.nevernote

import android.os.Bundle
import android.app.Activity

import kotlinx.android.synthetic.main.activity_map_view.*

class MapViewActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_view)

        val notesMapFragment = NotesMapFragment()
    }

}
