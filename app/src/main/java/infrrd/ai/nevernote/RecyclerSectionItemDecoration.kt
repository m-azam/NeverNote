package infrrd.ai.nevernote

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.recycler_section_header.view.*


class RecyclerSectionItemDecoration(val headerHeight:Int,val sticky: Boolean, val sectionCallback: SectionCallback) : RecyclerView.ItemDecoration() {

    var headerView: View? = null
    var header: TextView? = null

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        super.getItemOffsets(outRect, view, parent, state)
        var pos: Int = parent.getChildAdapterPosition(view)
        if (sectionCallback.isSection(pos)) {
            outRect.top = headerHeight
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if(headerView==null) {
            initHeader(parent)
        }

        var previousHeader: CharSequence = " "
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            initHeader(parent)

            val title = sectionCallback.getSectionHeader(position)
            header?.setText(title)
            if (previousHeader != title || sectionCallback.isSection(position)) {
                drawHeader(c,
                        child,
                        headerView as View,parent,title)
                previousHeader = title
            }
        }
    }

    fun initHeader(parent: RecyclerView) {
        headerView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_section_header,
                        parent,
                        false)
        header = headerView?.date_text
        fixLayoutSize(headerView as View,parent)
    }

    private fun drawHeader(c: Canvas, child: View, headerView: View, parent: RecyclerView, title:CharSequence) {
        c.save()
        c.translate(0f, Math.max(35, child.top - 65).toFloat())

        if(child.top <= 100) {
            var headerViewTop = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_section_header_top,
                            parent,
                            false)

            headerViewTop.date_text.text = title
            fixLayoutSize(headerViewTop as View,parent)
            headerViewTop.draw(c)
            c.restore()
            return
        }
        headerView.draw(c)
        c.restore()
    }

    private fun fixLayoutSize(view: View, parent: ViewGroup) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width,
                View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height,
                View.MeasureSpec.UNSPECIFIED)

        val childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                parent.paddingLeft + parent.paddingRight,
                view.layoutParams.width)
        val childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                parent.paddingTop + parent.paddingBottom,
                view.layoutParams.height)

        view?.measure(childWidth,
                childHeight)

        view?.layout(0,
                0,
                view.measuredWidth,
                view.measuredHeight)
    }

    interface SectionCallback {

        fun isSection(position: Int): Boolean

        fun getSectionHeader(position: Int): CharSequence
    }
}