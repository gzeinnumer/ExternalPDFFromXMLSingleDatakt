package com.gzeinnumer.externalpdffromxmlsingledatakt

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gzeinnumer.externalpdffromxmlsingledatakt.helper.FunctionGlobalPDFSingle
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(), FunctionGlobalPDFSingle.PDFCallBack {

    private val TAG = "MainActivity_"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = TAG

        val dataToPDF = HashMap<String, String>()

        dataToPDF["title"] = "Title ini"

        FunctionGlobalPDFSingle.initPDFSingleData(applicationContext, this, dataToPDF)
    }

    override fun callBackPath(path: String) {
        Log.d(TAG, "callBackPath: $path")
        sharePdf(path)
    }

    private fun sharePdf(fileName: String) {
        //kode ini penting ungutuk memaksa agar aplikasi luar bsa mengakses data yang kita butuh kan
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val uris = ArrayList<Uri>()
        val u = Uri.fromFile(File(fileName))
        uris.add(u)
        val sendToIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
        sendToIntent.type = "application/pdf"
        sendToIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        sendToIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        sendToIntent.putExtra(Intent.EXTRA_SUBJECT, "ini subject")
        sendToIntent.putExtra(Intent.EXTRA_TEXT, "ini message")
        sendToIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        try {
            startActivity(Intent.createChooser(sendToIntent, "Send mail..."))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Can't read pdf file", Toast.LENGTH_SHORT).show()
        }
    }
}