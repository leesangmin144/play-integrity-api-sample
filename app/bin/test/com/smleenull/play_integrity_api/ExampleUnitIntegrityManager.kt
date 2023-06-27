package com.smleenull.play_integrity_api

import com.smleenull.play_integrity_api.noncegenerator.AbstractNonceGenerator
import com.smleenull.play_integrity_api.noncegenerator.ClientNonceGenerator
import org.junit.Test

import org.junit.Assert.*
import java.util.Base64

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitIntegrityManager {

    @Test
    fun clientNonceGeneratorTest() {
        val clientNonceGenerator: AbstractNonceGenerator = ClientNonceGenerator()
        val b64Nonce = clientNonceGenerator.generateNonce("")

        val nonce = Base64.getUrlDecoder().decode(b64Nonce)

        val splitNonce = String(nonce).split(".")

        assertEquals(2, splitNonce.size)
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", splitNonce[0])
    }



}