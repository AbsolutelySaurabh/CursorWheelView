package github.hellocsl.cursorwheellayout.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View

import github.hellocsl.cursorwheel.CursorWheelLayout
import github.hellocsl.cursorwheellayout.R

class SimpleTextCursorWheelLayout(context: Context, attrs: AttributeSet) : CursorWheelLayout(context, attrs) {


    override fun onInnerItemSelected(v: View?) {
        super.onInnerItemSelected(v)
        if (v == null) {
            return
        }
        val tv = v.findViewById(R.id.wheel_menu_item_tv)
        tv.animate().scaleX(2f).scaleY(2f)
    }


    override fun onInnerItemUnselected(v: View?) {
        super.onInnerItemUnselected(v)
        if (v == null) {
            return
        }
        val tv = v.findViewById(R.id.wheel_menu_item_tv)
        tv.animate().scaleX(1f).scaleY(1f)
    }

    companion object {
        val MENU_COUNT = 10
        val INDEX_SPEC = 9
    }


}
