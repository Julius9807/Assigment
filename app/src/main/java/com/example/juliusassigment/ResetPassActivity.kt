package com.example.juliusassigment

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.juliusassigment.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView

@SuppressLint("CheckResult")
class ResetPassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //auth
        auth = FirebaseAuth.getInstance()

        //email validation
        val emailStream= RxTextView.textChanges(binding.etresetemail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            showEmailValidAlert(it)
        }

        //reset password
        binding.btnresetpage.setOnClickListener{
            val email = binding.etresetemail.text.toString().trim()
            auth.sendPasswordResetEmail(email).addOnCompleteListener(this){ reset ->
                    if (reset.isSuccessful){
                        Intent(this, LoginActivity::class.java).also{
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                            Toast.makeText(this, "Password have been reset, please check your email!", Toast.LENGTH_SHORT).show()
                        }
                }else {
                    Toast.makeText(this, reset.exception?.message, Toast.LENGTH_SHORT).show()
                }
                }
        }

        //click
        binding.tvresettologin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
    private fun showEmailValidAlert(isNotValid: Boolean){
        if (isNotValid){
            binding.etresetemail.error = "Email is not valid!"
            binding.btnresetpage.isEnabled = false
            binding.btnresetpage.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.darker_gray)}
        else{
            binding.etresetemail.error = null
            binding.btnresetpage.isEnabled = true
            binding.btnresetpage.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary_color)

        }
    }
}