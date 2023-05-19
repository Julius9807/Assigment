package com.example.juliusassigment

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.juliusassigment.databinding.ActivityChangenameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityChangenameBinding
private lateinit var auth: FirebaseAuth

class ChangeNameActivity : AppCompatActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangenameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()

        binding.backbtn.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.btnchangename.setOnClickListener{
            val newname = binding.etnewname.text.toString().trim()

            changeName(newname)
        }

        val nameStream = RxTextView.textChanges(binding.etnewname)
            .skipInitialValue()
            .map { email ->
                email.isEmpty()
            }
        nameStream.subscribe {
            showNameExistAlert(it)
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
    private fun showNameExistAlert(isNotValid: Boolean){
        if (isNotValid){
            binding.etnewname.error = "Name cannot be blank!"
            binding.btnchangename.isEnabled = false
            binding.btnchangename.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.darker_gray)}
        else{
            binding.etnewname.error = null
            binding.btnchangename.isEnabled = true
            binding.btnchangename.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary_color)

        }
    }
}