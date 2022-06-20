package br.com.contafacil.pagsegurotest

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.uol.pagseguro.plugpagservice.wrapper.*
import br.com.uol.pagseguro.plugpagservice.wrapper.listeners.PlugPagActivationListener


class MainActivity : AppCompatActivity(), PlugPagActivationListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startPayment(this)
    }
    fun startPayment(context: Context) {
        val paymentData = PlugPagPaymentData(
            PlugPag.TYPE_CREDITO,
            25000,
            PlugPag.INSTALLMENT_TYPE_A_VISTA,
            1,
            "CODVENDA"
        )
        val appIdentification = PlugPagAppIdentification("MeuApp", "1.0.7")
        val plugpag = PlugPag(context, appIdentification)
        //val listener = PagSeguroActivationListener()
        val initResult =plugpag.doAsyncInitializeAndActivatePinpad(
                PlugPagActivationData("403938"),
                this)
    }
    override fun onActivationProgress(data: PlugPagEventData) {
        val d = data
    }
    override fun onError(result: PlugPagInitializationResult) {
        val e = result
    }
    override fun onSuccess(result: PlugPagInitializationResult) {
        val s = result
    }
}