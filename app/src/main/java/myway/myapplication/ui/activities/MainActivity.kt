package myway.myapplication.ui.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_main.*
import myway.myapplication.R
import myway.myapplication.base.BaseActivity
import myway.myapplication.base.BaseViewModel
import myway.myapplication.base.initialFragment
import myway.myapplication.ui.BottomNavScreen
import myway.myapplication.utils.extensions.showGone
import myway.myapplication.utils.pereferences.SharedManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity : BaseActivity(R.layout.activity_main) {

    val viewModel by viewModel<BaseViewModel>()
    val sharedManager: SharedManager by inject()

    override fun onActivityCreated() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        viewModel.apply {
            parentLayoutId = R.id.fragmentContainer
            navLayoutId = R.id.navContainer

            fetchData()
        }

       // sharedManager.code = "4444" // todo

//        debug()
        startFragment()
    }

    private fun debug() {
       // initialFragment(SendTaskScreen())
    }

    private fun startFragment() {
        initialFragment(

            BottomNavScreen()
        )
    }

    fun showProgress(show: Boolean) {
        progressBar.showGone(show)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> return false
                KeyEvent.KEYCODE_VOLUME_DOWN -> return false
            }
        }
        return super.dispatchKeyEvent(event)
    }

    fun openPdf(path: String) {
        startActivity(Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(Uri.fromFile(File(path)), "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        })
    }

    fun createFileMultipart(name: String, file: File): MultipartBody.Part {
        return MultipartBody.Part.createFormData(name, file.name, file.asRequestBody("multipart/form-data".toMediaTypeOrNull()))
    }

    fun createBuilder(partName: String, file: File): RequestBody {
        val builder: MultipartBody.Builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart(
            partName, file.name, file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        )
        return builder.build()
    }

    fun getFilePath(it: Uri): String {
        //Later we will use this bitmap to create the File.
        val selectedBitmap = getBitmap(this, it)!!

        /*We can access getExternalFileDir() without asking any storage permission.*/
        val selectedImgFile = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            System.currentTimeMillis()
                .toString() + "_selectedImg.jpg"
        )

        convertBitmapToFile(selectedImgFile, selectedBitmap)

        return selectedImgFile.path
    }

    private fun getBitmap(context: Context, imageUri: Uri): Bitmap? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    context.contentResolver,
                    imageUri
                )
            )

        } else {
            context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        }
    }

    private fun convertBitmapToFile(destinationFile: File, bitmap: Bitmap) {
        //create a file to write bitmap data
        destinationFile.createNewFile()
        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos)
        val bitmapData = bos.toByteArray()
        //write the bytes in file
        val fos = FileOutputStream(destinationFile)
        fos.write(bitmapData)
        fos.flush()
        fos.close()
    }

}
