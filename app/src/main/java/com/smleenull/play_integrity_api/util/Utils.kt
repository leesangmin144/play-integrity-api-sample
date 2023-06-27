package com.smleenull.play_integrity_api.util

import kotlinx.serialization.json.JsonElement
import java.security.MessageDigest
import java.util.Base64

class Utils {


    fun b64Nonce(hashOfMsg: String, uniqueValue: String): String {

        return Base64.getUrlEncoder().withoutPadding().encodeToString("$hashOfMsg.$uniqueValue".toByteArray())
    }

    fun sha256(msgToHash: String): String {
        val bytes = msgToHash.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}