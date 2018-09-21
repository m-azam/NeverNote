package infrrd.ai.nevernote

class Note(val title:String,val body:String,val created: String, var selected: Boolean, var latitude:Double?, var longitude:Double?) {

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