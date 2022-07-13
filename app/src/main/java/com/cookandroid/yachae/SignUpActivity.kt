package com.cookandroid.yachae

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity {
    class MainActivity : AppCompatActivity() {
        private lateinit var auth: FirebaseAuth
        private val TAG = "SignUpActivity"

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_signup)
            // Initialize Firebase Auth
            auth = Firebase.auth

            var signUpButton : Button

            signUpButton = findViewById<Button>(R.id.signUpButton)

            signUpButton.setOnClickListener{
                Log.d("test log", "개발자용 로그")
                signUp()
            }
        }

        public override fun onStart() {
            super.onStart()
            val currentUser = auth.currentUser
        }

        public fun signUp() {
            var email : String
            var password : String

            email = findViewById<EditText>(R.id.email).text.toString()
            password = findViewById<EditText>(R.id.password).text.toString()

        auth.createUserWithEmailAndPassword(email, password)    //회원가입
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success")
                val user = auth.currentUser
                //UI
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                //UI
            }
        }
        }
    }
}