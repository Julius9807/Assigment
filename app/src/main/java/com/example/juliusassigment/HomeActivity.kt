package com.example.juliusassigment

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.juliusassigment.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val displayName = user?.displayName

        if (displayName != null) {
            val textView = findViewById<TextView>(R.id.tvHome)
            textView.text = "Welcome, $displayName!"
        } else {
            Toast.makeText(this, "Display name not set", Toast.LENGTH_SHORT).show()
        }


        binding.btnlogout.setOnClickListener{
            auth.signOut()
            Intent(this, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
                Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnchangename.setOnClickListener{
            startActivity(Intent(this, ChangeNameActivity::class.java))
        }


    }

}