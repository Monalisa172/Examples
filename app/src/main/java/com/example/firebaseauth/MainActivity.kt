package com.example.firebaseauth

import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firebaseauth.ui.theme.FireBaseAuthTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.firebaseauth.ViewModel.auth
import com.example.firebaseauth.ViewModel.callbacks
import com.example.firebaseauth.ViewModel.signIn
import com.example.firebaseauth.ViewModel.signUp
import com.example.firebaseauth.ViewModel.storedVerificationId
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            FireBaseAuthTheme {
                loginScreen()
            }
        }
    }
    fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOTP(otp: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, otp)
        signInWithPhoneAuthCredential(credential)
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
}


    @Composable
    fun loginScreen() {
        var phoneNumber by remember {
            mutableStateOf("")
        }
        var otp by remember {
            mutableStateOf("")
        }
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier.padding(10.dp).fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Card(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(20.dp)
                        .size(width = 500.dp, height = 330.dp)
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(17.dp)
                    ) {
                        TextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text(text = "Phone Number") },
                            modifier = Modifier
                                .clip(CircleShape)
                                .width(500.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { signUp(phoneNumber, otp) },
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Text(text = "Send OTP")
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        TextField(
                            value = otp,
                            onValueChange = { otp = it },
                            label = { Text(text = "Enter OTP") },
                            modifier = Modifier
                                .clip(CircleShape)
                                .width(500.dp),
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Button(
                            onClick = {
                                //verifyOTP(otp.value)
                            }
                        ) {
                            Text(text = "Validate OTP")
                        }
                    }
                }
            }
        }
    }
