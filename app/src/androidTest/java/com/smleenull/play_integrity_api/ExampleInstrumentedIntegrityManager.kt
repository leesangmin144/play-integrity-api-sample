package com.smleenull.play_integrity_api

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedIntegrityManager {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.smleenull.play_integrity_api", appContext.packageName)
    }

//    @Test
//    fun getIntegrityTokenTest() {
//
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//
//        val integrityManager = IntegrityManager()
//        integrityManager.getIntegrityToken(appContext)
//
//        assertEquals("1", "1")
//    }
}