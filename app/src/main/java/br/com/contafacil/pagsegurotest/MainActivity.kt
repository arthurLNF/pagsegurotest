package br.com.contafacil.pagsegurotest

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import br.com.uol.pagseguro.plugpagservice.wrapper.*
import br.com.uol.pagseguro.plugpagservice.wrapper.listeners.PlugPagActivationListener
import br.com.uol.pagseguro.plugpagservice.wrapper.listeners.PlugPagPaymentListener


class MainActivity : AppCompatActivity(), PlugPagActivationListener, PlugPagPaymentListener {
    lateinit var paymentData: PlugPagPaymentData
    val appIdentification = PlugPagAppIdentification("MeuApp", "1.0.7")
    val plugpag = PlugPag(this, appIdentification)
    lateinit var txtPagamento: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtPagamento = findViewById<TextView>(R.id.txtPagamento)
        startPayment(this)
    }

    fun startPayment(context: Context) {
        paymentData = PlugPagPaymentData(
            PlugPag.TYPE_CREDITO,
            25001,
            PlugPag.INSTALLMENT_TYPE_A_VISTA,
            1,
            "CODVENDA1"
        )

        val initResult = plugpag.doAsyncInitializeAndActivatePinpad(
            PlugPagActivationData("403938"),
            this
        )
    }

    override fun onActivationProgress(data: PlugPagEventData) {
        txtPagamento.text = data.customMessage
    }

    override fun onError(result: PlugPagInitializationResult) {
        txtPagamento.text = result.errorMessage
    }

    override fun onSuccess(result: PlugPagInitializationResult) {
        txtPagamento.text = result.errorMessage
        val paymentResult = plugpag.doAsyncPayment(paymentData, this)
    }

    override fun onError(it: PlugPagTransactionResult) {
        val s = it
        txtPagamento.text = it.message
    }

    override fun onPaymentProgress(eventData: PlugPagEventData) {
        txtPagamento.text = eventData.customMessage
    }

    override fun onPrinterError(printerResult: PlugPagPrintResult) {
        val p = printerResult
        txtPagamento.text = printerResult.message
    }

    override fun onPrinterSuccess(printerResult: PlugPagPrintResult) {
        val p = printerResult
        txtPagamento.text = printerResult.message
    }

    override fun onSuccess(it: PlugPagTransactionResult) {
        val result = it
        txtPagamento.text = it.message
    }
}