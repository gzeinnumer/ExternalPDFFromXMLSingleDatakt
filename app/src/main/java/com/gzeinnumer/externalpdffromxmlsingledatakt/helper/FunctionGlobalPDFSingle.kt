package com.gzeinnumer.externalpdffromxmlsingledatakt.helper

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import com.gzeinnumer.externalpdffromxmlsingledatakt.R
import com.hendrix.pdfmyxml.PdfDocument
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer
import java.io.File
import java.util.*


object FunctionGlobalPDFSingle {
    lateinit var pdfCallBack: PDFCallBack
    private const val TAG = "FunctionGlobalPDFSingle_"
    fun initPDFSingleData(
        context: Context,
        activity: Activity,
        dataToPDF: HashMap<String, String>
    ) {
        pdfCallBack = activity as PDFCallBack
        val page: AbstractViewRenderer =
            object : AbstractViewRenderer(context, R.layout.template_pdf) {
                override fun initView(view: View) {
                    val tvHello = view.findViewById<TextView>(R.id.tv)
                    tvHello.text = "Kirim Text Kesini " + dataToPDF["title"]
                }
            }
        val doc = PdfDocument(context)
        doc.addPage(page)
        //emang sudah default, value ini sama dengan ukuran pada xml nya
        doc.setRenderWidth(1500)
        doc.setRenderHeight(2115)
        doc.orientation = PdfDocument.A4_MODE.PORTRAIT
        doc.setProgressTitle(R.string.pdf_loading_title)
        doc.setProgressMessage(R.string.pdf_laoding_message)
        doc.fileName = "PDF File Name"
        doc.setSaveDirectory(File(FunctionGlobalDir.getStorageCard + FunctionGlobalDir.appFolder))
        doc.setInflateOnMainThread(false)
        doc.setListener(object : PdfDocument.Callback {
            override fun onComplete(file: File) {
                Log.i(
                    PdfDocument.TAG_PDF_MY_XML,
                    "Complete $file"
                )
                pdfCallBack.callBackPath(file.toString())
            }

            override fun onError(e: Exception) {
                Log.i(
                    PdfDocument.TAG_PDF_MY_XML,
                    "Error " + e.message
                )
            }
        })
        doc.createPdf(activity)
    }

    interface PDFCallBack {
        //pakai call back ini untuk membalikan path yang bsa dipakai untuk dishare
        fun callBackPath(path: String)
    }
}
