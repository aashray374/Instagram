package com.example.instagramclone

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instagramclone.Models.User
import com.example.instagramclone.Utils.USER_PROFILE_FOLDER
import com.example.instagramclone.Utils.uploadImage

import com.example.instagramclone.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Sign_up : AppCompatActivity() {
    val binding by lazy{
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private val launcher=registerForActivityResult(ActivityResultContracts.GetContent()){
        uri->
        uri?.let {
           uploadImage(uri, USER_PROFILE_FOLDER){
               if (it==null){

               }
               else{
                   user.image=it
                   binding.profileImage.setImageURI(uri)
               }
           }

        }
    }
    lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setContentView(binding.root)
        user=User()
        binding.register.setOnClickListener {
            if(binding.name.text.toString().equals("") or binding.email.text.toString().equals("") or binding.password.text.toString().equals(""))
            {
                Toast.makeText(this, "Please fill required details", Toast.LENGTH_SHORT).show()
            }
            else{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.email.text.toString(),binding.password.text.toString()
                ).addOnCompleteListener {
                    result->

                    if(result.isSuccessful)
                    {
                        user.name=binding.name.text.toString()
                        user.email=binding.email.text.toString()
                        user.password=binding.password.text.toString()
                        Firebase.firestore.collection("USER_NODE").document(Firebase.auth.currentUser!!.uid).set(user).addOnSuccessListener{
                           startActivity(Intent(this,HomeActivity::class.java))
                            finish()
                        }

                    }
                    else{
                        Toast.makeText(this, result.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.addImage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.login.setOnClickListener {
            startActivity(Intent(this,Login::class.java))
            finish()
        }
    }
}




