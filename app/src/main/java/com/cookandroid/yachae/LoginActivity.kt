package com.cookandroid.yachae

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var TAG : String = "LoginActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth


        var loginButton : Button
        var gotoSignUpButton :TextView

        loginButton = findViewById(R.id.loginButton)
        gotoSignUpButton = findViewById(R.id.gotoSignUpButton)


        // 추후에 코드 수정 예정. 함수 개수 줄이기 위함
        loginButton.setOnClickListener() {
            Log.d("test log", "개발자용 로그")
            signIn()
        }

        gotoSignUpButton.setOnClickListener {
            gotoSignUpActivity()
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
    }

    public fun signIn(){
        var email : String
        var password : String

        email = findViewById<EditText?>(R.id.emailEditText).text.toString()
        password = findViewById<EditText?>(R.id.passwordEditText).text.toString()

        // 이메일, 비번, 비번 확인 입력란 선택 시
        if(email.length>0 && password.length >0){
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        startToast("로그인에 성공했습니다.")
                        gotoMainActivity()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        startToast("로그인에 성공했습니다.")
                    }
                }
        }else{
            startToast("이메일 또는 비밀번호를 확인해 주세요.")
        }
    }

    private fun startToast(msg:String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


    private fun gotoMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun gotoSignUpActivity(){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }



}