package br.com.contafacil.pagsegurotest

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import br.com.uol.pagseguro.plugpagservice.wrapper.*
import com.github.danielfelgar.drawreceiptlib.ReceiptBuilder
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity(), PlugPagPrinterListener {
    val appIdentification = PlugPagAppIdentification("MeuApp", "1.0.7")
    val plugpag = PlugPag(this, appIdentification)
    lateinit var imgView: ImageView
    lateinit var btnImprimir: Button
    private lateinit var notinhaBitmap: Bitmap
    val path = "/sdcard/Download"
    //val path = Environment.getExternalStorageDirectory().absolutePath
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 1
        )
        imgView = findViewById(R.id.notinha)
        btnImprimir = findViewById(R.id.btn_imprimir)
        montarImagem()
        btnImprimir.setOnClickListener {
            imprimir(plugpag)
        }
    }

    private fun montarImagem() {
        val receipt = ReceiptBuilder(1200)
        receipt.setMargin(30, 20).setAlign(Paint.Align.CENTER).setColor(Color.BLACK)
            .setTextSize(60f).setTypeface(this, "fonts/RobotoMono-Regular.ttf")
            .addText("LakeFront Cafe").addText("1234 Main St.").addText("Palo Alto, CA 94568")
            .addText("999-999-9999").addBlankSpace(30).setAlign(Paint.Align.LEFT)
            .addText("Terminal ID: 123456", false).setAlign(Paint.Align.RIGHT).addText("1234")
            .setAlign(Paint.Align.LEFT).addLine().addText("08/15/16", false)
            .setAlign(Paint.Align.RIGHT).addText("SERVER #4").setAlign(Paint.Align.LEFT)
            .addParagraph().addText("CHASE VISA - INSERT").addText("AID: A000000000011111")
            .addText("ACCT #: *********1111").addParagraph()
            .setTypeface(this, "fonts/RobotoMono-Regular.ttf").addText("CREDIT SALE")
            .addText("UID: 12345678", false).setAlign(Paint.Align.RIGHT).addText("REF #: 1234")
            .setTypeface(this, "fonts/RobotoMono-Regular.ttf").setAlign(Paint.Align.LEFT)
            .addText("BATCH #: 091", false).setAlign(Paint.Align.RIGHT).addText("AUTH #: 0701C")
            .setAlign(Paint.Align.LEFT).addParagraph()
            .setTypeface(this, "fonts/RobotoMono-Regular.ttf").addText("AMOUNT", false)
            .setAlign(Paint.Align.RIGHT).addText("$ 15.00").setAlign(Paint.Align.LEFT)
            .addParagraph().addText("TIP", false).setAlign(Paint.Align.RIGHT).addText("$        ")
            .addLine(180).setAlign(Paint.Align.LEFT).addParagraph().addText("TOTAL", false)
            .setAlign(Paint.Align.RIGHT).addText("$        ").addLine(180).addParagraph()
            .setAlign(Paint.Align.CENTER).setTypeface(this, "fonts/RobotoMono-Regular.ttf")

        notinhaBitmap = receipt.build()
        salvarImagensToInternalStorage("/recibo.png", notinhaBitmap)

        val fileName = "$path/recibo.png";
        val imgFile = File(fileName)

        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            val myImage = findViewById<View>(R.id.notinha) as ImageView
            myImage.setImageBitmap(myBitmap)
        }
    }

    private fun salvarImagensToInternalStorage(fileName: String?, imagem: Bitmap) {

        val dir = File(path + fileName!!)
        if (!dir.exists()) dir.parentFile?.mkdirs()
        try {
//            val bmp = BitmapFactory.decodeByteArray(imagem, 0, imagem.length)
            val stream = ByteArrayOutputStream()
            imagem.compress(Bitmap.CompressFormat.PNG, 90, stream)
            val image = stream.toByteArray()

            val fos = FileOutputStream(dir)
            fos.flush()
            fos.write(image)
            fos.close()
        } catch (e: IOException) {
            Log.w("InternalStorage", "Error writing", e)
        }
    }

    fun imprimir(plugPag: PlugPag?) {
        val data = PlugPagPrinterData(
            "$path/recibo.png",
            4,
            10 * 12
        )

        plugPag?.setPrinterListener(this)

        plugPag?.printFromFile(data)
    }

    override fun onError(data: PlugPagPrintResult) {
        val d = data
    }

    override fun onSuccess(data: PlugPagPrintResult) {
        val d = data
    }

}