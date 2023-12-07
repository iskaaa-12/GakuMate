package com.labactivity.gakumate

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.labactivity.gakumate.databinding.ActivityEditProfileBinding
import java.io.File

class EditProfile : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri
    private lateinit var user: Users
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference.child("users") // Assuming "Users" is the root node

        val uid = auth.currentUser?.uid.toString()

        loadProfileImage()
        getUserData()


        binding.saveId.setOnClickListener {
            val username = binding.editUsername.text.toString()

            if (uid.isNotEmpty()) {
                if(binding.editUsername.text.isEmpty()){
                    binding.editUsername.error="Username can't be empty!"
                }else {
                    databaseReference.child(uid).child("username").setValue(username)
                        .addOnCompleteListener { databaseUpdateTask ->
                            if (databaseUpdateTask.isSuccessful) {
                                // Database update successful, now update authentication
                                uploadProfile()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Failed to update username in the database!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
                val intent = Intent(this, UserProfileActivity::class.java)
                startActivity(intent)
            }
        }

        binding.userProfile.setOnClickListener {
            // Assuming imageUri is set correctly
            openGallery()
        }
    }

    private fun uploadProfile() {
        val username = binding.editUsername.text.toString()

        if (imageUri != null) {
            // If the user selected a new profile picture
            storageReference =
                FirebaseStorage.getInstance()
                    .getReference("users/${auth.currentUser?.uid}/profile.jpg")

            storageReference.putFile(imageUri)
                .addOnCompleteListener { uploadTask ->
                    if (uploadTask.isSuccessful) {
                        // Profile picture uploaded successfully
                        storageReference.downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()

                            // Update user's profile image URL
                            updateProfilePicture(imageUrl)

                            // Load and display the user's profile image using Picasso
                            loadProfileImage()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Failed to get the profile picture download URL!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Failed to upload the profile picture!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            updateAuthentication(username, null)
        }
    }

    private fun updateProfilePicture(imageUrl: String) {
        auth.currentUser?.updateProfile(
            UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(imageUrl))
                .build()
        )?.addOnCompleteListener { profileUpdateTask ->
            if (profileUpdateTask.isSuccessful) {
                Toast.makeText(this, "Profile picture updated successfully!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Failed to update profile picture!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateAuthentication(username: String, imageUrl: String?) {
        val profileUpdateBuilder = UserProfileChangeRequest.Builder()
            .setDisplayName(username)

        imageUrl?.let {
            profileUpdateBuilder.setPhotoUri(Uri.parse(it))
        }

        auth.currentUser?.updateProfile(profileUpdateBuilder.build())
            ?.addOnCompleteListener { profileUpdateTask ->
                if (profileUpdateTask.isSuccessful) {
                    if (imageUrl != null) {
                        Toast.makeText(
                            this,
                            "Username and profile picture updated successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this, "Username updated successfully!", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Failed to update username and profile picture!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    private fun loadProfileImage() {
        // Load user profile image using Picasso
        storageReference =
            FirebaseStorage.getInstance().reference.child("users/${auth.currentUser?.uid}/profile.jpg")

        val localFile = File.createTempFile("tempImage", "jpg")
        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.userProfile.setImageBitmap(bitmap)
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to retrieve image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the selected image URI
            imageUri = data.data!!

            if (imageUri != null) {
                // Show confirmation dialog after selecting the image
                val confirmDialog = AlertDialog.Builder(this)
                    .setTitle("Change Profile Picture")
                    .setMessage("Are you sure you want to change your profile picture?")
                    .setPositiveButton("Yes") { _, _ ->
                        // User confirmed, proceed with profile picture change
                        uploadProfile()
                    }
                    .setNegativeButton("No") { _, _ ->
                        // User canceled, do nothing
                    }
                    .create()

                confirmDialog.show()
            } else {
                Toast.makeText(this, "Failed to get the selected image URI", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun getUserData() {
        databaseReference.child(auth.currentUser?.uid.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                user = snapshot.getValue(Users::class.java)!!
                binding.editUsername.setText(user.username)
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

