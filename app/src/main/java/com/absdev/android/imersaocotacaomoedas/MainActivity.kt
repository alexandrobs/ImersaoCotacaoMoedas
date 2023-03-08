package com.absdev.android.imersaocotacaomoedas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.absdev.android.imersaocotacaomoedas.api.CotacaoAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var btnRecuperar: Button
    lateinit var txtBitcoin: TextView
    lateinit var txtEthereum: TextView

    val cotacaoApi: CotacaoAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.mercadobitcoin.net/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CotacaoAPI::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnRecuperar = findViewById(R.id.btn_recuperar)
        txtBitcoin = findViewById(R.id.text_bitcoin)
        txtEthereum = findViewById(R.id.textEthereum)

        btnRecuperar.setOnClickListener {

            //precisa de permissao de internet
            CoroutineScope(Dispatchers.IO).launch {
                val respostaBtc = cotacaoApi.recuperarCotacaoBitcoin()
                if (respostaBtc.isSuccessful) {
                    var cotacao = respostaBtc.body()
                    if (cotacao != null) {
                        withContext(Dispatchers.Main) {
                            txtBitcoin.text = "Bitcoin R$ ${cotacao.ticker.last}"
                        }
                    }
                }

                val respostaEth = cotacaoApi.recuperarCotacaoEthereum()
                if (respostaEth.isSuccessful) {
                    var cotacao = respostaEth.body()
                    if (cotacao != null) {
                        withContext(Dispatchers.Main) {
                            txtEthereum.text = "Ethereum R$ ${cotacao.ticker.last}"
                        }
                    }
                }
            }
        }
    }
}