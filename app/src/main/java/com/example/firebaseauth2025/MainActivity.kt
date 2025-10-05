package com.example.firebaseauth2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat.enableEdgeToEdge
import com.example.firebaseauth2025.ui.theme.FirebaseAuth2025Theme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebaseAuth2025Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AuthScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// connected to Birdwatching project in Firebase
// authentication method Email/Password enabled
@Composable
fun AuthScreen(modifier: Modifier = Modifier) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            )
        )
        Row {
            Button(onClick = {
                // TODO validate email and password
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            message =
                                "Sign up successful: ${auth.currentUser?.email ?: "unknown"}"
                        } else {
                            message =
                                "Sign up failed: ${task.exception?.localizedMessage ?: "unknown error"}"
                        }
                    }
            }) {
                Text("Sign Up")
            }
            Button(onClick = {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        message = if (task.isSuccessful) {
                            "Sign in successful: ${auth.currentUser?.email ?: "unknown"}"
                        } else {
                            "Sign in failed: ${task.exception?.localizedMessage ?: "unknown error"}"
                        }
                    }
            }) {
                Text("Sign In")
            }
        }
        Text(message)
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    FirebaseAuth2025Theme {
        AuthScreen()
    }
}