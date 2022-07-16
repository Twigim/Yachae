package com.cookandroid.yachae

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterInfoActivity : AppCompatActivity() {

    lateinit var register_image_btn : Button
    lateinit var register_info_btn: Button
    lateinit var nickname_editText : EditText



    //private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_info)

        register_image_btn = findViewById<Button>(R.id.register_image_btn)
        register_info_btn = findViewById(R.id.register_info_btn)
        nickname_editText = findViewById(R.id.nickname_editText)


        register_image_btn.setOnClickListener {
            Log.d("RegisterInfoActivity", "이미지를 선택해주세요~~~!!")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        register_info_btn.setOnClickListener {
            Log.d("RegisterInfoActivity","닉네임이랑 프로필 설정까지하고 확인~~!!")
            performRegister()
            gotoMainActivity()
        }



    }

    var selectedImageUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("RegisterInfoActivity","이미지 선택됨!!!!")

            selectedImageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            register_image_btn.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun performRegister(){
        uploadImageToFirebaseStorage()

    }

    private fun uploadImageToFirebaseStorage(){
        if(selectedImageUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedImageUri!!).addOnSuccessListener{
            Log.d("RegisterInfoActivity", "이미지 업로드 성공적!!: ${it.metadata?.path}")

            ref.downloadUrl.addOnSuccessListener {
                Log.d("RegisterInfoActivity", "파일 위치!!: $it")

                saveUserInfoToFirebaseDatabase(it.toString())

            }
        }
    }

    private fun saveUserInfoToFirebaseDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        var user = User2(uid.toString(), nickname_editText.text.toString(), profileImageUrl)

        ref.setValue(user).addOnSuccessListener {
            Log.d("RegisterInfoActivity", "firebase에 유저 정보 저장 성공!!")
        }

    }

    private fun gotoMainActivity(){
        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)

    }


}
class User2(val uid:String, val nickname: String, val profileImageUrl: String)

