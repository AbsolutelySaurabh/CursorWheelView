package github.hellocsl.cursorwheellayout.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import java.util.ArrayList

import github.hellocsl.cursorwheellayout.R
import github.hellocsl.cursorwheellayout.data.Model_images


class Adapter_PhotosFolder(internal var context: Context, al_menu: ArrayList<Model_images>) : ArrayAdapter<Model_images>(context, R.layout.adapter_photosfolder, al_menu) {
    internal lateinit var viewHolder: ViewHolder
    internal var al_menu = ArrayList<Model_images>()


    init {
        this.al_menu = al_menu


    }

    override fun getCount(): Int {

        Log.e("ADAPTER LIST SIZE", al_menu.size.toString() + "")
        return al_menu.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getViewTypeCount(): Int {
        return if (al_menu.size > 0) {
            al_menu.size
        } else {
            1
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        if (convertView == null) {

            viewHolder = ViewHolder()
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_photosfolder, parent, false)
            viewHolder.tv_foldern = convertView!!.findViewById(R.id.tv_folder) as TextView
            viewHolder.tv_foldersize = convertView.findViewById(R.id.tv_folder2) as TextView
            viewHolder.iv_image = convertView.findViewById(R.id.iv_image) as ImageView


            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.tv_foldern!!.text = al_menu[position].str_folder
        viewHolder.tv_foldersize!!.text = al_menu[position].al_imagepath.size.toString() + ""



        Glide.with(context).load("file://" + al_menu[position].al_imagepath[0])
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(viewHolder.iv_image!!)


        return convertView

    }

    class ViewHolder {
        internal var tv_foldern: TextView? = null
        internal var tv_foldersize: TextView? = null
        internal var iv_image: ImageView? = null


    }


}