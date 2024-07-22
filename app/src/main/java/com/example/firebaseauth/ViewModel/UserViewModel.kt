package com.example.firebaseauth.ViewModel


import com.google.firebase.auth.FirebaseAuth

val auth = FirebaseAuth.getInstance()
fun signIn(email: String, password: String) {
    auth.signInWithEmailAndPassword(email,password)
        .addOnSuccessListener { task ->
            if (task.user != null) {
                println("User Created")
                val user = auth.currentUser
                println(user?.uid)
            }
        }
}

fun signUp(email: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnSuccessListener { task ->
            if (task.user != null) {
                println("User Created")
                val user = auth.currentUser
                println(user?.uid)
            }
        }
}