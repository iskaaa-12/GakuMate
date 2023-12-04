package com.labactivity.gakumate


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.labactivity.gakumate.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        setSignIn()

        setTermsAndPolicyText()

        binding.signupId.setOnClickListener {

            val username = binding.usernameId.text.toString()
            val email = binding.emailId.text.toString()
            val password = binding.passwordId.text.toString()
            val confirmPass = binding.confirmId.text.toString()
            val terms = binding.termsId

            if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()){
                if(username.isEmpty()){
                    binding.usernameIL.error = "Username is Required!"
                }
                if(email.isEmpty()){
                    binding.emailIL.error = "Email is Required!"
                }
                if(password.isEmpty()){
                    binding.passwordIL.error = "Password is Required!"
                }
                if(confirmPass.isEmpty()){
                    binding.confirmIL.error = "Re-enter Password!"
                }
            }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.emailIL.error = "Please Enter Valid Email Address"

            }else if(!terms.isChecked){

                Toast.makeText(this, "Please agree to the Terms and Conditions to continue", Toast.LENGTH_SHORT).show()

            }else if(password.length < 6){
                binding.passwordIL.error = "Password needs to be at least 6 characters long!"
            }else if(password != confirmPass){

                binding.passwordIL.error = "Password does not match!"
                binding.confirmIL.error = "Password does not match!"

            }else{
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        val databaseRef = database.reference.child("users").child(auth.currentUser!!.uid)
                        val users : Users = Users(username,email,auth.currentUser!!.uid)

                        databaseRef.setValue(users).addOnCompleteListener {
                            if(it.isSuccessful){
                                val intent = Intent(this, SignInActivity::class.java)
                                startActivity(intent)
                            }else{
                                Toast.makeText(this, "User data not saved!", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }else{
                        if (it.exception?.message?.contains("email address is already in use") == true) {
                            // Handle the case where the email is already in use
                            binding.emailIL.error = "Email is already registered"
                        } else {
                            // Handle other registration errors
                            Toast.makeText(this, "Something went wrong! Try again!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


    }

    private fun setTermsAndPolicyText() {
        val termsAndPolicyText = getString(R.string.terms_conditions)
        val spannableString = SpannableString(termsAndPolicyText)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                openTermsAndPolicy()
            }
        }

        val startIndex = termsAndPolicyText.indexOf("Terms and Conditions")
        val endIndex = startIndex + "Terms and Conditions".length

        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.termsTxt.text = spannableString
        binding.termsTxt.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun openTermsAndPolicy() {
        val intent = Intent(this, terms::class.java)
        startActivity(intent)
    }

    private fun setSignIn() {
        val signInText = getString(R.string.sign_in)
        val spannableString = SpannableString(signInText)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                openSignIn()
            }
        }

        val startIndex = signInText.indexOf("Sign In")
        val endIndex = startIndex + "Sign In".length

        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.signinTxt.text = spannableString
        binding.signinTxt.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun openSignIn() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}