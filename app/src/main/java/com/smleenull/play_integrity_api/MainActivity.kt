package com.smleenull.play_integrity_api

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.smleenull.play_integrity_api.ui.theme.PlayintegrityapiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlayintegrityapiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    RunPlayIntegrityAPI(this.applicationContext, MainViewModel())
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunPlayIntegrityAPI(applicationContext: Context, viewModel: MainViewModel) {

    var ipAddr by remember { mutableStateOf("192.168.0.28") }

    val connectionStatus by viewModel.connectionStatus.observeAsState()
    val randomValue by viewModel.randomValue.observeAsState()
    val nonceValue by viewModel.nonceValue.observeAsState()
    val checkIntegrityTokenResult by viewModel.checkIntegrityTokenResult.observeAsState()


    Column {
        TextField(
            value = ipAddr,
            onValueChange = { ipAddr = it },
            label = { Text("IP Address") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(onClick = {
            viewModel.checkConnection(ipAddr)
        }) {
            Text("Connect")
        }
        Text(connectionStatus ?: "")


        Button(onClick = { viewModel.getRandomValue(ipAddr) }) {
            Text("Generate nonce")
        }
//        Text(randomValue ?: "")
//        Text(nonceValue ?: "")


        Button(onClick = { viewModel.checkIntegrityToken(applicationContext, ipAddr) }) {
            Text("Check Integrity by Play Integrity API")
        }
        Text(checkIntegrityTokenResult ?: "")

    }
}