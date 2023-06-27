package com.smleenull.play_integrity_api

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.ColumnScope
import com.google.android.gms.tasks.Task
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.google.android.play.core.integrity.IntegrityTokenResponse
import com.smleenull.play_integrity_api.noncegenerator.ClientNonceGenerator
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

import kotlin.coroutines.suspendCoroutine

class IntegrityManager {
    private val TAG = "smleenull"
    val client = OkHttpClient()

    fun getNonce(msgToProtect: String): String {
        // TODO: Change NonceGenerator to ServerNonceGenerator
        return ClientNonceGenerator().generateNonce(msgToProtect)
    }

    fun getIntegrityToken(applicationContext: Context) {

        // TODO: Give a message to getNonce()
        // Receive the nonce from the secure server.
        val nonce: String = getNonce("")

        // Create an instance of a manager.
        val integrityManager = IntegrityManagerFactory.create(applicationContext)

        // Request the integrity token by providing a nonce.
        val integrityTokenResponse: Task<IntegrityTokenResponse> =
            integrityManager.requestIntegrityToken(
                IntegrityTokenRequest.builder()
                    .setNonce(nonce)
                    .setCloudProjectNumber(86750613080)
                    .build())

        integrityTokenResponse.addOnSuccessListener { response ->
            // This block is called when the task successfully completes.
            // The response is available here.
            Log.d("IntegrityToken", "Response: ${response.token()}")

            // This block is called when the task successfully completes.
            // The response is available here.
            Log.d("IntegrityToken", "Response: ${response.token()}")

            // Your server URL
            val url = "http://192.168.0.21:8888/a/integrity/verify/integrity-token"

            // JSON 객체 생성
            val jsonParams = JSONObject()
            jsonParams.put("integrityToken", response.token())

            // JSON으로 변환
            val jsonData = jsonParams.toString()

            // JSON 형식의 RequestBody 생성
            val requestBody: RequestBody = jsonData
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    response.body?.string()?.let { Log.d("Server Response", it) }
                }
            })
        }

        integrityTokenResponse.addOnFailureListener { exception ->
            // This block is called when the task fails.
            // The exception is available here.
            Log.e("IntegrityToken", "Error requesting integrity token", exception)
        }

//        integrityTokenResponse.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                // The task is successful, and the result is available.
//                val response = task.result
//                Log.d("IntegrityToken", "Response: $response")
//            } else {
//                // The task failed. The exception is available.
//                val exception = task.exception
//                Log.e("IntegrityToken", "Error requesting integrity token", exception)
//            }
//        }


    }


}