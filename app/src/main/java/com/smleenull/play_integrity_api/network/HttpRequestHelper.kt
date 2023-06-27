package com.smleenull.play_integrity_api.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

object HttpRequestHelper {
    private val client = OkHttpClient()

    fun createGetRequest(url: String): Request {
        return Request.Builder()
            .url(url)
            .get()
            .build()
    }

    fun createPostRequest(url: String, requestBody: RequestBody): Request {
        return Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
    }

    suspend fun sendRequest(request: Request, callback: Callback) {
        client.newCall(request).enqueue(callback)
    }
}