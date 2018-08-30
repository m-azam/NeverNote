package infrrd.ai.nevernote

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem

class ActionBarCallBack(private val onExitSelectionListener: OnExitSelectionListener): ActionMode.Callback {


    interface OnExitSelectionListener {
        fun onExitSelection()
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        return false
    }

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        mode.menuInflater.inflate(R.menu.context_menu, menu)
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        onExitSelectionListener.onExitSelection()
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return false
    }
}