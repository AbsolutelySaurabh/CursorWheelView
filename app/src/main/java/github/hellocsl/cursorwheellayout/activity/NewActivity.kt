//package github.hellocsl.cursorwheellayout.activity
//
//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.database.Cursor
//import android.net.Uri
//import android.provider.MediaStore
//import android.support.v4.app.ActivityCompat
//import android.support.v4.content.ContextCompat
//import android.support.v7.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.AdapterView
//import android.widget.GridView
//import android.widget.Toast
//
//import java.util.ArrayList
//
//import github.hellocsl.cursorwheellayout.R
//import github.hellocsl.cursorwheellayout.adapter.Adapter_PhotosFolder
//import github.hellocsl.cursorwheellayout.data.Model_images
//
//class NewActivity : AppCompatActivity() {
//    internal var boolean_folder: Boolean = false
//    internal lateinit var obj_adapter: Adapter_PhotosFolder
//    internal lateinit var gv_folder: GridView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_new)
//        gv_folder = (findViewById(R.id.gv_folder) as GridView?)!!
//
//        gv_folder.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
//            val intent = Intent(applicationContext, NewActivity::class)
//            intent.putExtra("value", i)
//            startActivity(intent)
//        }
//
//
//        if (ContextCompat.checkSelfPermission(applicationContext,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(applicationContext,
//                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this@NewActivity,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this@NewActivity,
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//            } else {
//                ActivityCompat.requestPermissions(this@NewActivity,
//                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
//                        REQUEST_PERMISSIONS)
//            }
//        } else {
//            Log.e("Else", "Else")
//            fn_imagespath()
//        }
//
//
//    }
//
//    fun fn_imagespath(): ArrayList<Model_images> {
//        al_images.clear()
//
//        var int_position = 0
//        val uri: Uri
//        val cursor: Cursor?
//        val column_index_data: Int
//        val column_index_folder_name: Int
//
//        var absolutePathOfImage: String? = null
//        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//
//        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
//
//        val orderBy = MediaStore.Images.Media.DATE_TAKEN
//        cursor = applicationContext.contentResolver.query(uri, projection, null, null, orderBy + " DESC")
//
//        column_index_data = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
//        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
//        while (cursor.moveToNext()) {
//            absolutePathOfImage = cursor.getString(column_index_data)
//            Log.e("Column", absolutePathOfImage)
//            Log.e("Folder", cursor.getString(column_index_folder_name))
//
//            for (i in al_images.indices) {
//                if (al_images[i].str_folder == cursor.getString(column_index_folder_name)) {
//                    boolean_folder = true
//                    int_position = i
//                    break
//                } else {
//                    boolean_folder = false
//                }
//            }
//
//
//            if (boolean_folder) {
//
//                val al_path = ArrayList<String>()
//                al_path.addAll(al_images[int_position].al_imagepath)
//                al_path.add(absolutePathOfImage)
//                al_images[int_position].al_imagepath = al_path
//
//            } else {
//                val al_path = ArrayList<String>()
//                al_path.add(absolutePathOfImage)
//                val obj_model = Model_images()
//                obj_model.str_folder = cursor.getString(column_index_folder_name)
//                obj_model.al_imagepath = al_path
//
//                al_images.add(obj_model)
//
//
//            }
//
//
//        }
//
//
//        for (i in al_images.indices) {
//            Log.e("FOLDER", al_images[i].str_folder)
//            for (j in 0 until al_images[i].al_imagepath.size) {
//                Log.e("FILE", al_images[i].al_imagepath[j])
//            }
//        }
//        obj_adapter = Adapter_PhotosFolder(this@NewActivity, al_images)
//        gv_folder.adapter = obj_adapter
//        return al_images
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        when (requestCode) {
//            REQUEST_PERMISSIONS -> {
//                for (i in grantResults.indices) {
//                    if (grantResults.size > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                        fn_imagespath()
//                    } else {
//                        Toast.makeText(this@NewActivity, "The app was not allowed to read or write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
//        }
//    }
//
//    companion object {
//
//        var al_images = ArrayList<Model_images>()
//        private val REQUEST_PERMISSIONS = 100
//    }
//
//}