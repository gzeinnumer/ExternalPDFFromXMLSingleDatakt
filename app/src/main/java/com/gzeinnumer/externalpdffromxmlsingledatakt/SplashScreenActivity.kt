package com.gzeinnumer.externalpdffromxmlsingledatakt

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gzeinnumer.externalpdffromxmlsingledatakt.helper.FunctionGlobalDir
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.util.*

class SplashScreenActivity : AppCompatActivity() {

    private val TAG = "SplashScreenActivity_"

    var permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    var msg = "externalpdffromxmlsingledatakt\n"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        title = TAG

        if (checkPermissions()) {
            msg += "Izin diberikan\n"
            tv.text = msg
            onSuccessCheckPermitions()
        } else {
            msg += "Beri izin dulu\n"
            tv.text = msg
        }
    }

    private fun onSuccessCheckPermitions() {
        if (FunctionGlobalDir.initFolder()) {
            if (FunctionGlobalDir.isFileExists(FunctionGlobalDir.appFolder)) {
                msg += "Sudah bisa lanjut\n"
                tv.text = msg
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                msg += "Direktory tidak ditemukan\n"
                tv.text = msg
            }
        } else {
            msg += "Gagal membuat folder\n"
            tv.text = msg
        }
    }

    var MULTIPLE_PERMISSIONS = 1
    private fun checkPermissions(): Boolean {
        var result: Int
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(applicationContext, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == MULTIPLE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onSuccessCheckPermitions()
            } else {
                val perStr = StringBuilder()
                for (per in permissions) {
                    perStr.append("\n").append(per)
                }
            }
        }
    }
}