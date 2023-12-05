package com.labactivity.gakumate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.labactivity.gakumate.databinding.ActivitySignInBinding
class SignInActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        setSignUp()

        binding.loginId.setOnClickListener {
            val email = binding.emailLi.text.toString()
            val password = binding.passwordLi.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                if (email.isEmpty()) {
                    binding.emailIL.error = "Please Enter your Email!"
                }
                if (password.isEmpty()) {
                    binding.passwordIL.error = "Please Enter Password!"
                }
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailIL.error = "Please Enter Valid Email Address"

            } else if (password.length < 6) {
                binding.passwordIL.error = "Password needs to be at least 6 characters long!"
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

    }


    private fun setSignUp() {
        val signUpText = getString(R.string.sign_up)
        val spannableString = SpannableString(signUpText)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                openSignUp()
            }
        }

        val startIndex = signUpText.indexOf("Sign Up")
        val endIndex = startIndex + "Sign Up".length

        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.signupTxt.text = spannableString
        binding.signupTxt.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun openSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}