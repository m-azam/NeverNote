package infrrd.ai.nevernote

import android.os.Parcelable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import android.os.Parcel



class Note(val title:String,val body:String,val created: Date, var selected: Boolean): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            TODO("created"),
            parcel.readByte() != 0.toByte()) {
    }

    fun isSelected(): Boolean {
        return selected
    }

    fun onSelect() {
        selected = true
    }

    fun onDeselect() {
        selected = false
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeString(title)
        out.writeString(body)
        out.writeString(created.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }

}