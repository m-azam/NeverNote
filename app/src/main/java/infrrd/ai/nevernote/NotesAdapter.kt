package infrrd.ai.nevernote

import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.recycler_cell_layout.view.body
import kotlinx.android.synthetic.main.recycler_cell_layout.view.checkbox_multiselect
import kotlinx.android.synthetic.main.recycler_cell_layout.view.title
import org.jsoup.Jsoup

class NotesAdapter(private val editNote: ((position:Int,note:Note)->Unit)?,private val actionBarCallback: ActionBarCallback,
                   private val context: Context, private val notesDataset: MutableList<Note>)
    : RecyclerView.Adapter<NotesAdapter.MyViewHolder>(), ActionBarCallBack.OnExitSelectionListener,Filterable {

    var multiSelect: Boolean = false
    var selectCount: Int = 0
    var selectedArray: ArrayList<Int> = arrayListOf()
    var filteredNotes: MutableList<Note> = notesDataset
    var trashNotes: MutableList<Note> = ArrayList()

    interface ActionBarCallback {
        var actionMode: ActionMode?
        fun startActionBar()
        fun finishActionBar()
    }


    inner class MyViewHolder(val note: RelativeLayout) :  RecyclerView.ViewHolder(note), View.OnLongClickListener, View.OnClickListener {

        init {
            note.setOnLongClickListener(this)
            note.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            if (multiSelect) {

                note.checkbox_multiselect.isChecked = !filteredNotes[adapterPosition].isSelected()
                if (filteredNotes[adapterPosition].isSelected()) {
                    filteredNotes[adapterPosition].onDeselect()
                    selectedArray.remove(adapterPosition)
                    view?.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_light))
                    selectCount -= 1
                    if(selectCount == 0) {
                        actionBarCallback.finishActionBar()
                    }
                } else {
                    filteredNotes[adapterPosition].onSelect()
                    selectedArray.add(adapterPosition)
                    view?.setBackgroundColor(ContextCompat.getColor(context, R.color.settings_background_on_touch))
                    selectCount += 1
                }
            }
            else {
                editNote?.let {
                    it(adapterPosition,filteredNotes[adapterPosition])
                }
            }
        }

        override fun onLongClick(view: View?): Boolean {
            if (!multiSelect) {
                multiSelect = true
                filteredNotes[adapterPosition].onSelect()
                selectedArray.add(adapterPosition)
                view?.setBackgroundColor(ContextCompat.getColor(context, R.color.settings_background_on_touch))
                selectCount += 1
                actionBarCallback.startActionBar()
                multiSelect = true
                notifyDataSetChanged()
            }
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapter.MyViewHolder {
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_cell_layout, parent, false) as RelativeLayout
        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.note.title.text = filteredNotes[position].title
        var parsedString: String = Jsoup.parse(filteredNotes[position].body).text()
        var outputBody: String
        if( parsedString.length > 90) {
            outputBody = parsedString.substring(0,87) + "..."
        }
        else {
            outputBody = parsedString
        }

        holder.note.body.text = outputBody
        holder.note.checkbox_multiselect.visibility = if (multiSelect) View.VISIBLE else View.GONE
        holder.note.checkbox_multiselect.isChecked = filteredNotes[position].isSelected()
        if (filteredNotes[position].isSelected()) {
            holder.note.setBackgroundColor(ContextCompat.getColor(context, R.color.settings_background_on_touch))
        } else {
            holder.note.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_light))
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(filterCharSequence: CharSequence?): FilterResults {
                val filterString = filterCharSequence.toString()
                if (filterString.isEmpty()) {
                    filteredNotes = notesDataset
                } else {
                    val temporaryFilteredNotesSet = arrayListOf<Note>()
                    for (note in notesDataset) {
                        if ((note.title.toLowerCase().contains(filterString.toLowerCase()))
                                || (note.body.toLowerCase().contains(filterString.toLowerCase()))) {
                            temporaryFilteredNotesSet.add(note)
                        }
                    }
                    filteredNotes = temporaryFilteredNotesSet
                }
                val filterResults = FilterResults()
                filterResults.values = filteredNotes
                return filterResults
            }

            override fun publishResults(filterCharSequence: CharSequence?, filterResults: FilterResults?) {
                filteredNotes = filterResults?.values as MutableList<Note>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount() = filteredNotes.size

    override fun onExitSelection() {
        for(index in selectedArray) {
            filteredNotes[index].onDeselect()
        }
        notifyDataSetChanged()
        selectCount = 0
        multiSelect = false
    }
}