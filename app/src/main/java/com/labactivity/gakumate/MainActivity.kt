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


        add.setOnClickListener {
            val dialogBuilder = DialogPlus.newDialog(this)
                .setContentHolder(ViewHolder(R.layout.show_add))
                .setExpanded(false) // This is optional, set it as per your requirement
                .create()

            // Show the dialog
            dialogBuilder.show()
        }

        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                // User is not logged in, navigate to SignInActivity
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
            // User is already logged in, continue with the current activity
        }

        // Register the auth state listener
        auth.addAuthStateListener(authListener)

        binding.img1.setOnClickListener{

            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the auth state listener to avoid memory leaks
        auth.removeAuthStateListener(authListener)
    }

}