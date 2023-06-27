package com.smleenull.play_integrity_api

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.google.android.play.core.integrity.IntegrityTokenResponse
import com.smleenull.play_integrity_api.util.Utils
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class IntegrityManager {
    private val TAG = "smleenull"
    private val client = OkHttpClient()

    fun getNonce(ipAddr: String, msgToProtect: String, _randomValue: MutableLiveData<String>, _nonceValue: MutableLiveData<String>) {

        val hashedMsgToProtect: String = Utils().sha256(msgToProtect)

        val baseUrl = "http://${ipAddr}:8888"
        val request = Request.Builder()
            .url("${baseUrl}/a/integrity/fetch/random-value")
            .get()
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                _randomValue.postValue("Request for checking failed")
            }

            override fun onResponse(call: Call, response: Response) {
                val bodyString = response.body?.string()
                _randomValue.postValue(if (response.isSuccessful) "Request successful\n $bodyString" else "Request failed")

                if (bodyString != null) {
                    val jsonObject = Json.parseToJsonElement(bodyString).jsonObject
                    val random = jsonObject["random"].toString()
                    _nonceValue.postValue(Utils().b64Nonce(hashedMsgToProtect, random))
                }

            }
        })
    }

    fun getIntegrityToken(applicationContext: Context,
                          ipAddr: String,
                          _checkIntegrityTokenResult: MutableLiveData<String>,
                          _nonceValue: MutableLiveData<String>) {

        val baseUrl = "http://${ipAddr}:8888"

        // TODO: Give a message to getNonce()
        // Receive the nonce from the secure server.

        val nonce: String = _nonceValue.value.toString()

        if (nonce.isNullOrEmpty() || nonce == "null") {
            Log.d(TAG, "SDFSAD")
            _checkIntegrityTokenResult.postValue("Fetch nonce from the server. Click the 'generate nonce' button")
        }

        // Create an instance of a manager.
        val integrityManager = IntegrityManagerFactory.create(applicationContext)

        // Request the integrity token by providing a nonce.
        // TODO: Change fetch param of setCloudProjectNumber from config file
        try {
            val integrityTokenResponse: Task<IntegrityTokenResponse> =
                integrityManager.requestIntegrityToken(
                    IntegrityTokenRequest.builder()
                        .setNonce(nonce)
                        .setCloudProjectNumber(86750613080)
                        .build()
                )


            integrityTokenResponse.addOnSuccessListener { response ->
                // This block is called when the task successfully completes.
                // The response is available here.
                Log.d("IntegrityToken", "Response: ${response.token()}")

                // This block is called when the task successfully completes.
                // The response is available here.
                Log.d("IntegrityToken", "Response: ${response.token()}")

                // JSON 객체 생성
                val jsonParams = JSONObject()
                jsonParams.put("integrityToken", response.token())

                // JSON으로 변환
                val jsonData = jsonParams.toString()
                // JSON 형식의 RequestBody 생성
                val requestBody: RequestBody = jsonData.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                val request = Request.Builder()
                    .url("${baseUrl}/a/integrity/verify/integrity-token")
                    .post(requestBody)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        _checkIntegrityTokenResult.postValue("Request for checking failed")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val bodyString = response.body?.string()

                        _checkIntegrityTokenResult.postValue(if (response.isSuccessful) "$bodyString" else "Request failed")
                    }
                })

            }
        } catch (e: Exception) {
            e.message?.let { Log.d(TAG, it) }
        }

    }

}