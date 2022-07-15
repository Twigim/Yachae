package com.cookandroid.yachae

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity {
    class MainActivity : AppCompatActivity() {
        private lateinit var auth: FirebaseAuth
        private var googleSignInClient : GoogleSignInClient? = null
        private val TAG = "SignUpActivity"
        var GOOGLE_LOGIN_CODE = 9001

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_signup)

            // Initialize Firebase Auth
            auth = Firebase.auth
            var signUpButton : Button
            var googleSignUnButton : Button

            signUpButton = findViewById<Button>(R.id.signUpButton)
            signUpButton.setOnClickListener{
                Log.d("test log", "개발자용 로그")
                signUpEmail()
            }

            googleSignUnButton = findViewById<Button>(R.id.googleSignUnButton)
            googleSignUnButton.setOnClickListener {
                //1
                googleLogin()
            }
            var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(this, gso)
        }

        public fun signUpEmail() {
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

        public override fun onStart() {
            super.onStart()
            val currentUser = auth.currentUser
        }

        fun googleLogin() {
            var signInIntent = googleSignInClient?.signInIntent
            startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
            Log.d("로그인", "로그인")
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if(requestCode == GOOGLE_LOGIN_CODE) {
                //구글에서 제공하는 로그인 결과값 받아오기
                var result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
                // result가 성공했을 때 이 값을 firebase에 넘겨주기
                if(result!!.isSuccess) {
                    var account = result.signInAccount
                    // 2
                    firebaseAuthWithGoogle(account)
                }
            }
        }

        fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
            var credential = GoogleAuthProvider.getCredential(account?.idToken,null)
            auth?.signInWithCredential(credential)
                ?.addOnCompleteListener {
                        task ->
                    if(task.isSuccessful) {
                        //Login, 아이디와 패스워드가 맞았을 때
                        Toast.makeText(this,  "success", Toast.LENGTH_LONG).show()
                        val userintent = Intent(this, UserInfoActivity::class.java)
                        startActivity(userintent)
                    } else {
                        // Show the error message, 아이디와 패스워드가 틀렸을 때
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }


    }
}