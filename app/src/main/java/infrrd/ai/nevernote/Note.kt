package infrrd.ai.nevernote

import android.location.Location
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import android.os.Parcel



class Note(val title:String,val body:String,val created: Date, var selected: Boolean, var latitude:Double?, var longitude:Double?) {

    fun isSelected(): Boolean {
        return selected
    }

    fun onSelect() {
        selected = true
    }

    fun onDeselect() {
        selected = false
    }
}