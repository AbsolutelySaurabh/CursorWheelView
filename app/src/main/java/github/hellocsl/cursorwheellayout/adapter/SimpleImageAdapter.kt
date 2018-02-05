package github.hellocsl.cursorwheellayout.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import github.hellocsl.cursorwheel.CursorWheelLayout
import github.hellocsl.cursorwheellayout.R
import github.hellocsl.cursorwheellayout.data.ImageData

class SimpleImageAdapter(private val mContext: Context, private val mMenuItemDatas: List<ImageData>?) : CursorWheelLayout.CycleWheelAdapter() {
    private val mLayoutInflater: LayoutInflater

    init {
        mLayoutInflater = LayoutInflater.from(mContext)
    }

    override fun getCount(): Int {
        return mMenuItemDatas?.size ?: 0
    }

    override fun getView(parent: View, position: Int): View {
        val item = getItem(position)
        val root = mLayoutInflater.inflate(R.layout.wheel_image_item, null, false)
        val imgView = root.findViewById(R.id.wheel_menu_item_iv) as ImageView


        Glide.with(mContext).load("file://" + item.getmImageRes_1())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imgView)

        // Toast.makeText(mContext, item.getmImageRes_1() , Toast.LENGTH_SHORT).show();


        //imgView.setImageResource(item.mImageRes);
        return root
    }

    override fun getItem(position: Int): ImageData {
        return mMenuItemDatas!![position]
    }

}
