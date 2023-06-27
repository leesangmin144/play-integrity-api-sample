package com.smleenull.play_integrity_api.noncegenerator

import java.security.MessageDigest
import java.util.Base64

abstract class AbstractNonceGenerator {
    abstract fun getRandom(): String

    fun generateNonce(msgToProtect: String): String {

        // Get hash of the message to protect
        val hashOfMsg: String = msgToProtect.sha256()

        // Get Random
        val uniqueValue: String = getRandom()

        // Concatenate hash of the message and csprng
        return b64Nonce(hashOfMsg, uniqueValue)
    }

    private fun String.sha256(): String {
        val bytes = this.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    private fun b64Nonce(hashOfMsg: String, uniqueValue: String): String {

        return Base64.getUrlEncoder().withoutPadding().encodeToString("$hashOfMsg.$uniqueValue".toByteArray())
    }
}