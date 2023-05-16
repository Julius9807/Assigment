package com.example.juliusassigment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.juliusassigment.databinding.ActivityRegisterAcitivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable


@SuppressLint("CheckResult")
class RegisterAcitivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterAcitivityBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAcitivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

//Auth
    auth = FirebaseAuth.getInstance()



//name validation
        val nameStream = RxTextView.textChanges(binding.etregistername)
            .skipInitialValue()
            .map { name ->
                name.length < 6
            }
            nameStream.subscribe{
            showTextMinimalAlert(it,"Name")
        }

//email validation
        val emailStream= RxTextView.textChanges(binding.etregisteremail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            showEmailValidAlert(it)
        }

//password validation
        val passwordStream =  RxTextView.textChanges(binding.etregisterpassword)
            .skipInitialValue()
            .map { password ->
                password.length < 8
            }
        passwordStream.subscribe{
            showTextMinimalAlert(it,"Password")
        }

//confirm password validation
        val passwordConfirmStream = Observable.merge(
            RxTextView.textChanges(binding.etregisterpassword)
                .skipInitialValue()
                .map { password ->
                    password.toString() != binding.etregisterpasswordconfirm.text.toString()
                },
            RxTextView.textChanges(binding.etregisterpasswordconfirm)
                .skipInitialValue()
                .map { confirmPassword ->
                    confirmPassword.toString() != binding.etregisterpassword.text.toString()
                }
        )
        passwordConfirmStream.subscribe{
            showPasswordConfirmAlert(it)
        }
//button enable true or false
        val invalidFieldStream = Observable.combineLatest(
            nameStream,
            emailStream,
            passwordStream,
            passwordConfirmStream,
         { nameInvalid: Boolean, emailInvalid: Boolean, passwordInvalid: Boolean, passwordConfirmInvalid: Boolean ->
            !nameInvalid && !emailInvalid && !passwordInvalid && !passwordConfirmInvalid
        })
        invalidFieldStream.subscribe{
            isValid ->
            if (isValid){
                binding.btnregisterpage.isEnabled = true
                binding.btnregisterpage.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primary_color)}
            else{
                binding.btnregisterpage.isEnabled = false
                binding.btnregisterpage.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.darker_gray)}
        }



//click
        binding.btnregisterpage.setOnClickListener{
            val email = binding.etregisteremail.text.toString().trim()
            val password = binding.etregisterpassword.text.toString().trim()
            registerUser(email, password)
        }

        binding.tvregistertologin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }


    }
    private fun showNameExistAlert(isNotValid: Boolean){
        binding.etregistername.error = if (isNotValid) "Name cannot be blank!" else null
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String){
        if (text == "Name")
            binding.etregistername.error = if (isNotValid) "$text have to be 6 or more digit!" else null
        else if (text == "Password")
            binding.etregisterpassword.error = if (isNotValid) "$text have to be 8 or more digit!" else null
    }

    private fun showEmailValidAlert(isNotValid: Boolean){
        binding.etregisteremail.error = if (isNotValid) "Email is not valid!" else null
    }

    private fun showPasswordConfirmAlert(isNotValid: Boolean){
        binding.etregisterpasswordconfirm.error = if(isNotValid) "Password not match!" else null
    }

    private fun registerUser(email:String , password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    startActivity(Intent(this, LoginActivity::class.java))
                    Toast.makeText(this, "Register Successfully", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

}