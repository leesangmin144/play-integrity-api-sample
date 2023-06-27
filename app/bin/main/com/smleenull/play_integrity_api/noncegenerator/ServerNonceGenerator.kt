package com.smleenull.play_integrity_api.noncegenerator


class ServerNonceGenerator : AbstractNonceGenerator() {

    // Fetch CSPRNG(Cryptographically Secure Pseudo Random Number) from server
    override fun getRandom(): String {

        TODO("Fetch 32bytes and base64-encoded CSPRNG from server")
    }
}