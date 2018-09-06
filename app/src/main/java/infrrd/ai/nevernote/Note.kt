package infrrd.ai.nevernote


import java.util.*


class Note(val title:String,val body:String,val created: Date, var selected: Boolean){


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