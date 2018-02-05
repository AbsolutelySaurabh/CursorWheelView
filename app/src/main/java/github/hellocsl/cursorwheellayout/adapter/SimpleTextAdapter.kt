package github.hellocsl.cursorwheellayout.adapter

import android.content.Context
import android.support.v4.app.ActivityCompat
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

import github.hellocsl.cursorwheel.CursorWheelLayout
import github.hellocsl.cursorwheellayout.R
import github.hellocsl.cursorwheellayout.data.MenuItemData

class SimpleTextAdapter constructor(private val mContext: Context, private val mMenuItemDatas: List<MenuItemData>?, private val mGravity: Int = Gravity.CENTER) : CursorWheelLayout.CycleWheelAdapter() {
    private val mLayoutInflater: LayoutInflater

    init {
        mLayoutInflater = LayoutInflater.from(mContext)
    }

    override fun getCount(): Int {
        return mMenuItemDatas?.size ?: 0
    }

    override fun getView(parent: View, position: Int): View {
        val item = getItem(position)
        val root = mLayoutInflater.inflate(R.layout.wheel_menu_item, null, false)
        val textView = root.findViewById(R.id.wheel_menu_item_tv) as TextView
        textView.visibility = View.VISIBLE
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        textView.text = item.mTitle
        if (textView.layoutParams is FrameLayout.LayoutParams) {
            (textView.layoutParams as FrameLayout.LayoutParams).gravity = mGravity
        }
        if (position == INDEX_SPEC) {
            textView.setTextColor(ActivityCompat.getColor(mContext, R.color.red))
        }
        return root
    }

    override fun getItem(position: Int): MenuItemData {
        return mMenuItemDatas!![position]
    }

    companion object {
        val INDEX_SPEC = 9
    }

}
