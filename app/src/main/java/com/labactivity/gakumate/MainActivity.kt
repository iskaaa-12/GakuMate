package com.labactivity.gakumate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.labactivity.gakumate.databinding.ActivityMainBinding
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance() // Initialize FirebaseAuth


        val add = binding.btnAdd

        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Register the auth state listener
        auth.addAuthStateListener(authListener)

        add.setOnClickListener {
            val dialogBuilder = DialogPlus.newDialog(this)
                .setContentHolder(ViewHolder(R.layout.show_add))
                .setExpanded(false) // This is optional, set it as per your requirement
                .create()

            // Show the dialog
            dialogBuilder.show()
        }

        binding.img1.setOnClickListener{
            navigateToUserProfile()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToUserProfile() {
        val intent = Intent(this, UserProfileActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the auth state listener to avoid memory leaks
        auth.removeAuthStateListener(authListener)
    }
}
