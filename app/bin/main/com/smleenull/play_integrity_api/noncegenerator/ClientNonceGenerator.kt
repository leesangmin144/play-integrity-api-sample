package com.smleenull.play_integrity_api.noncegenerator

import java.security.SecureRandom
import java.util.Base64

class ClientNonceGenerator : AbstractNonceGenerator() {

    // Generate CSPRNG(Cryptographically Secure Pseudo Random Number)
    override fun getRandom(): String {

        val secureRandom = SecureRandom()
        val nonceBytes = ByteArray(32)
        secureRandom.nextBytes(nonceBytes)

        // base64-encoded URL-safe & no-wrap
        // ref: https://developer.android.com/google/play/integrity/verdict?hl=ko#nonce
        return nonceBytes.fold("") { str, it -> str + "%02x".format(it) }
    }

}