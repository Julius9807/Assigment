package com.example.juliusassigment

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.juliusassigment.databinding.ActivityHomeBinding
import com.example.juliusassigment.databinding.ActivityMain2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityMain2Binding
private lateinit var auth: FirebaseAuth

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnchangename.setOnClickListener{
            val newname = binding.etnewname.text.toString().trim()

            changeName(newname)
        }


    }
    private fun changeName(newname:String){
        val user = FirebaseAuth.getInstance().currentUser
        val newDisplayName = binding.etnewname.text.toString()

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newDisplayName)
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    Toast.makeText(this, "Display name updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error updating display name", Toast.LENGTH_SHORT).show()
                }
            }

    }
}