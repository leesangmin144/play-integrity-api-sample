package com.smleenull.play_integrity_api

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainViewModel : ViewModel() {

    private val _connectionStatus = MutableLiveData<String>()
    val connectionStatus: LiveData<String> = _connectionStatus

    private val _randomValue = MutableLiveData<String>()
    val randomValue: LiveData<String> = _randomValue

    private val _nonceValue = MutableLiveData<String>()
    val nonceValue: LiveData<String> = _nonceValue

    private val _checkIntegrityTokenResult = MutableLiveData<String>()
    val checkIntegrityTokenResult: LiveData<String> = _checkIntegrityTokenResult

    fun checkConnection(ipAddr: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("http://${ipAddr}:8888/hello")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    _connectionStatus.postValue("Failure: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        _connectionStatus.postValue("Success: ${response.body?.string()}")
                    } else {
                        _connectionStatus.postValue("Error: ${response.message}")
                    }
                }
            })
        }
    }

    fun getRandomValue(ipAddr: String) {
        IntegrityManager().getNonce(ipAddr, "", _randomValue, _nonceValue)
    }

    fun checkIntegrityToken(applicationContext: Context, ipAddr: String) {
        IntegrityManager().getIntegrityToken(applicationContext, ipAddr, _checkIntegrityTokenResult, _nonceValue)
    }
}