package github.hellocsl.cursorwheellayout.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import java.util.ArrayList
import java.util.Random

import butterknife.Bind
import butterknife.ButterKnife
import github.hellocsl.cursorwheel.CursorWheelLayout
import github.hellocsl.cursorwheellayout.R
import github.hellocsl.cursorwheellayout.adapter.Adapter_PhotosFolder
import github.hellocsl.cursorwheellayout.adapter.SimpleImageAdapter
import github.hellocsl.cursorwheellayout.data.ImageData
import github.hellocsl.cursorwheellayout.data.Model_images

class MainActivity : AppCompatActivity(), CursorWheelLayout.OnMenuSelectedListener {

    internal var mRandom = Random()
    internal var imageView: ImageView? = null
    internal var boolean_folder: Boolean = false
    var obj_adapter: Adapter_PhotosFolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        if (ContextCompat.checkSelfPermission(applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_PERMISSIONS)
            }
        } else {
            Log.e("Else", "Else")
            fn_imagespath()
        }
    }


    fun fn_imagespath(): ArrayList<Model_images> {
        al_images.clear()

        var int_position = 0
        val uri: Uri
        val cursor: Cursor?
        val column_index_data: Int
        val column_index_folder_name: Int

        var absolutePathOfImage: String? = null
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        val orderBy = MediaStore.Images.Media.DATE_TAKEN
        cursor = applicationContext.contentResolver.query(uri, projection, null, null, orderBy + " DESC")

        column_index_data = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)
            Log.e("Column", absolutePathOfImage)
            Log.e("Folder", cursor.getString(column_index_folder_name))

            for (i in al_images.indices) {
                if (al_images[i].str_folder == cursor.getString(column_index_folder_name)) {
                    boolean_folder = true
                    int_position = i
                    break
                } else {
                    boolean_folder = false
                }
            }


            if (boolean_folder) {

                val al_path = ArrayList<String>()
                al_path.addAll(al_images[int_position].al_imagepath)
                al_path.add(absolutePathOfImage)
                al_images[int_position].al_imagepath = al_path

            } else {
                val al_path = ArrayList<String>()
                al_path.add(absolutePathOfImage)
                val obj_model = Model_images()
                obj_model.str_folder = cursor.getString(column_index_folder_name)
                obj_model.al_imagepath = al_path

                al_images.add(obj_model)

            }
        }


        for (i in al_images.indices) {
            Log.e("FOLDER", al_images[i].str_folder)
            for (j in 0 until al_images[i].al_imagepath.size) {
                Log.e("FILE", al_images[i].al_imagepath[j])
            }
        }
        // obj_adapter = new Adapter_PhotosFolder(MainActivity.this,al_images);
        //gv_folder.setAdapter(obj_adapter);

        if (al_images.size != 0) {
            initData()
        }
        return al_images
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                for (i in grantResults.indices) {
                    if (grantResults.size > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        fn_imagespath()
                    } else {
                        Toast.makeText(this@MainActivity, "The app was not allowed to read or write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    private fun initData() {

        val t = findViewById(R.id.id_wheel_menu_center_item) as ImageView?
        val imageDatas = ArrayList<ImageData>()
        val folder_name = findViewById(R.id.folder_name) as TextView?
        var mTestCircleMenuTop: CursorWheelLayout? = findViewById(R.id.test_circle_menu_top) as CursorWheelLayout?;


        for (i in al_images.indices) {

            imageDatas.add(ImageData(al_images[i].al_imagepath[0], i.toString()))

        }

        val simpleImageAdapter = SimpleImageAdapter(this, imageDatas)
        mTestCircleMenuTop!!.setAdapter(simpleImageAdapter)
        mTestCircleMenuTop!!.setOnMenuSelectedListener { parent, view, pos ->
            Glide.with(applicationContext).load("file://" + al_images[pos].al_imagepath[0])
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(t!!)

            folder_name!!.text = getFolderName(al_images[pos].al_imagepath[0])
        }
       // mTestCircleMenuTop!!.setOnMenuItemClickListener { view, pos -> startActivity(Intent(applicationContext, NewActivity::class.java)) }
    }

    fun getFolderName(path: String): String {

        val both = path.split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        return both[both.size - 2]
    }

    override fun onItemSelected(p: CursorWheelLayout, view: View, pos: Int) {}

    companion object {

        var al_images = ArrayList<Model_images>()
        //GridView gv_folder;
        private val REQUEST_PERMISSIONS = 100

        fun getPath(context: Context, uri: Uri): String {
            var result: String? = null
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(uri, proj, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val column_index = cursor.getColumnIndexOrThrow(proj[0])
                    result = cursor.getString(column_index)
                }
                cursor.close()
            }
            if (result == null) {
                result = "Not found"
            }
            return result
        }
    }

}
