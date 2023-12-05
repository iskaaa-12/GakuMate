package com.labactivity.gakumate

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.labactivity.gakumate.databinding.ActivitySignInBinding
import com.labactivity.gakumate.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var user: Users
    private lateinit var uid:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        uid = auth.currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        if(uid.isNotEmpty()){

            getUserData()
        }

        binding.signOut.setOnClickListener {
            auth.signOut()
            signOutUser()
        }

        binding.aboutUs.setOnClickListener {
            val intent = Intent(this, About_app::class.java)
            startActivity(intent)

        }


    }

    private fun signOutUser() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Sign Out")
        alertDialogBuilder.setMessage("Are you sure you want to sign out?")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            // User clicked Yes, sign out
            auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            // User clicked No, dismiss the dialog
            dialog.dismiss()
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun getUserData(){
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                user = snapshot.getValue(Users::class.java)!!
                binding.dispUser.setText(user.username)
            }

            override fun onCancelled(error: DatabaseError) {
                onError()
            }

        })
    }

    private fun onError(){
        Toast.makeText(this, "Something went  Wrong!", Toast.LENGTH_SHORT).show()
    }

}