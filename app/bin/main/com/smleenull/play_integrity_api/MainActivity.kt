package com.smleenull.play_integrity_api

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

                    RunPlayIntegrityAPI(this.applicationContext)
                }
            }
        }
    }
}

@Composable
fun RunPlayIntegrityAPI(applicationContext: Context) {
    var nonceResult by remember { mutableStateOf("") }
    var integrityTokenResult by remember { mutableStateOf("") }

    Column {
        Button(onClick = { nonceResult = IntegrityManager().getNonce("") }) {
            Text("Click to generate nonce")
        }
        Text(nonceResult)

        Button(onClick = { IntegrityManager().getIntegrityToken(applicationContext) }) {
            Text("Call Play Integrity API")
        }
        Text(integrityTokenResult)

    }
}

//@Preview
//@Composable
//fun PreviewRunPlayIntegrityAPI() {
//    RunPlayIntegrityAPI(this.applicationContext)
//}