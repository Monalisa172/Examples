package com.example.firebaseauth.ViewModel


import android.os.Build
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

val auth = FirebaseAuth.getInstance()
fun signIn(email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                println("User Logged In")
                var user = auth.currentUser
                println(user?.uid)
            }
        }
}
fun signUp(email: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener() { task->

            if (task.isSuccessful != null) {
                println("User Created")
                var user = auth.currentUser
                println(user?.uid)
            }
        }
}


var storedVerificationId: String? = null
lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
val callbacks = object:PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        // This callback will be invoked in two situations:
        // 1 - Instant verification. In some cases the phone number can be instantly
        //     verified without needing to send or enter a verification code.
        // 2 - Auto-retrieval. On some devices Google Play services can automatically
        //     detect the incoming verification SMS and perform verification without
        //     user action.
        Log.d(TAG, "onVerificationCompleted:$credential")
        //signInWithPhoneAuthCredential(credential)
    }

    override fun onVerificationFailed(e: FirebaseException) {
        // This callback is invoked in an invalid request for verification is made,
        // for instance if the the phone number format is not valid.
        Log.w(TAG, "onVerificationFailed", e)

        if (e is FirebaseAuthInvalidCredentialsException) {
            // Invalid request
        } else if (e is FirebaseTooManyRequestsException) {
            // The SMS quota for the project has been exceeded
        } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
            // reCAPTCHA verification attempted with null Activity
        }
        // Show a message and update the UI
    }

    override fun onCodeSent(
        verificationId: String,
        token: PhoneAuthProvider.ForceResendingToken,
    ) {
        // The SMS verification code has been sent to the provided phone number, we
        // now need to ask the user to enter the code and then construct a credential
        // by combining the code with a verification ID.
        Log.d(TAG, "onCodeSent:$verificationId")

        // Save verification ID and resending token so we can use them later
        storedVerificationId = verificationId
        resendToken = token
    }
}

