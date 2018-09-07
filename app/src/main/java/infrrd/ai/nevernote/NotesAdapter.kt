package infrrd.ai.nevernote

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.RelativeLayout
import jp.wasabeef.richeditor.RichEditor
import kotlinx.android.synthetic.main.recycler_cell_layout.view.*
import org.jsoup.Jsoup

class NotesAdapter(private val actionBarCallback: ActionBarCallback, private val context: Context, private val myDataset: MutableList<Note>)
    : RecyclerView.Adapter<NotesAdapter.MyViewHolder>(), ActionBarCallBack.OnExitSelectionListener, Filterable {

    var multiSelect: Boolean = false
    var selectCount: Int = 0
    var selectedArray: ArrayList<Int> = arrayListOf()

    interface ActionBarCallback {
        var actionMode: ActionMode?
        fun startActionBar()
        fun finishActionBar()
    }

    inner class MyViewHolder(val note: RelativeLayout) : RecyclerView.ViewHolder(note), View.OnLongClickListener, View.OnClickListener {

        init {
            note.setOnLongClickListener(this)
            note.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            if (multiSelect) {
                note.checkbox_multiselect.isChecked = !myDataset[adapterPosition].isSelected()
                if (myDataset[adapterPosition].isSelected()) {
                    myDataset[adapterPosition].onDeselect()
                    selectedArray.remove(adapterPosition)
                    view?.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_light))
                    selectCount -= 1
                    if(selectCount == 0) {
                        actionBarCallback.finishActionBar()
                    }
                } else {
                    myDataset[adapterPosition].onSelect()
                    selectedArray.add(adapterPosition)
                    view?.setBackgroundColor(ContextCompat.getColor(context, R.color.settings_background_on_touch))
                    selectCount += 1
                }
            }
        }

        override fun onLongClick(view: View?): Boolean {
            if (!multiSelect) {
                multiSelect = true
                myDataset[adapterPosition].onSelect()
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
        holder.note.title.text = myDataset[position].title
        holder.note.body.text = Jsoup.parse(myDataset[position].body).text()
        holder.note.checkbox_multiselect.visibility = if (multiSelect) View.VISIBLE else View.GONE
        holder.note.checkbox_multiselect.isChecked = myDataset[position].isSelected()
        if (myDataset[position].isSelected()) {
            holder.note.setBackgroundColor(ContextCompat.getColor(context, R.color.settings_background_on_touch))
        } else {
            holder.note.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_light))
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(filterCharSequence: CharSequence?): FilterResults {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun publishResults(filterCharSequence: CharSequence?, filterResults: FilterResults?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }

    override fun getItemCount() = myDataset.size

    override fun onExitSelection() {
        for(index in selectedArray) {
            myDataset[index].onDeselect()
        }
        notifyDataSetChanged()
        selectCount = 0
        multiSelect = false
    }
}