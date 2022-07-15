package com.cookandroid.yachae

import android.graphics.Bitmap
import android.icu.text.Transliterator.getDisplayName
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text


class UserInfoActivity : AppCompatActivity() {

    //사용자 정보 알기 위해 auth
    private var mAuth: FirebaseAuth? = null
    //프로필 uri -> bitmap
    var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_info)

        //로그인 사용자 정보
        mAuth = FirebaseAuth.getInstance();
        val user = Firebase.auth.currentUser

        //제공업체로부터 사용자 프로필 정보 가져오기
        user?.let {
            for (profile in it.providerData) {
                // Name, email address, and profile photo Url
                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl
            }
        }

        val username : TextView = findViewById(R.id.username)
        username.setText(user!!.displayName + "의 페이지")
        username.setVisibility(View.VISIBLE)

    }
}