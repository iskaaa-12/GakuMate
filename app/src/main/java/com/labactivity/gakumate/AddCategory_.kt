package com.labactivity.gakumate

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.labactivity.gakumate.databinding.ActivityAddCategoryBinding


class AddCategory_ : AppCompatActivity() {
    private lateinit var binding: ActivityAddCategoryBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var categories: ArrayList<TheCategory>
    private lateinit var adapter: CategoryAdapter
    private var selectedColor: Int = android.R.color.transparent // Default transparent color

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categories = intent.getSerializableExtra("categories") as? ArrayList<TheCategory> ?: ArrayList()
        adapter = CategoryAdapter(categories)
        databaseHelper = DatabaseHelper(this)

        binding.color1.setOnClickListener {
            binding.imgNoteC1.setImageResource(R.drawable.ic_tick)
            binding.imgNoteC2.setImageResource(0)
            binding.imgNoteC3.setImageResource(0)
            binding.imgNoteC4.setImageResource(0)
            binding.imgNoteC5.setImageResource(0)
            binding.imgNoteC6.setImageResource(0)
            binding.imgNoteC7.setImageResource(0)
            binding.imgNoteC8.setImageResource(0)
            onColorButtonClick(R.color.blueNote)
        }

        binding.color2.setOnClickListener {
            binding.imgNoteC2.setImageResource(R.drawable.ic_tick)
            binding.imgNoteC1.setImageResource(0)
            binding.imgNoteC3.setImageResource(0)
            binding.imgNoteC4.setImageResource(0)
            binding.imgNoteC5.setImageResource(0)
            binding.imgNoteC6.setImageResource(0)
            binding.imgNoteC7.setImageResource(0)
            binding.imgNoteC8.setImageResource(0)
            onColorButtonClick(R.color.yellowNote)
        }

        binding.color3.setOnClickListener {
            binding.imgNoteC3.setImageResource(R.drawable.ic_tick)
            binding.imgNoteC1.setImageResource(0)
            binding.imgNoteC2.setImageResource(0)
            binding.imgNoteC4.setImageResource(0)
            binding.imgNoteC5.setImageResource(0)
            binding.imgNoteC6.setImageResource(0)
            binding.imgNoteC7.setImageResource(0)
            binding.imgNoteC8.setImageResource(0)
            onColorButtonClick(R.color.greenNote)
        }

        binding.color4.setOnClickListener {
            binding.imgNoteC4.setImageResource(R.drawable.ic_tick)
            binding.imgNoteC1.setImageResource(0)
            binding.imgNoteC3.setImageResource(0)
            binding.imgNoteC2.setImageResource(0)
            binding.imgNoteC5.setImageResource(0)
            binding.imgNoteC6.setImageResource(0)
            binding.imgNoteC7.setImageResource(0)
            binding.imgNoteC8.setImageResource(0)
            onColorButtonClick(R.color.indigoNote)
        }

        binding.color5.setOnClickListener {
            binding.imgNoteC5.setImageResource(R.drawable.ic_tick)
            binding.imgNoteC1.setImageResource(0)
            binding.imgNoteC3.setImageResource(0)
            binding.imgNoteC4.setImageResource(0)
            binding.imgNoteC2.setImageResource(0)
            binding.imgNoteC6.setImageResource(0)
            binding.imgNoteC7.setImageResource(0)
            binding.imgNoteC8.setImageResource(0)
            onColorButtonClick(R.color.redNote)
        }

        binding.color6.setOnClickListener {
            binding.imgNoteC6.setImageResource(R.drawable.ic_tick)
            binding.imgNoteC1.setImageResource(0)
            binding.imgNoteC3.setImageResource(0)
            binding.imgNoteC4.setImageResource(0)
            binding.imgNoteC5.setImageResource(0)
            binding.imgNoteC2.setImageResource(0)
            binding.imgNoteC7.setImageResource(0)
            binding.imgNoteC8.setImageResource(0)
            onColorButtonClick(R.color.orangeNote)
        }

        binding.color7.setOnClickListener {
            binding.imgNoteC7.setImageResource(R.drawable.ic_tick)
            binding.imgNoteC1.setImageResource(0)
            binding.imgNoteC3.setImageResource(0)
            binding.imgNoteC4.setImageResource(0)
            binding.imgNoteC5.setImageResource(0)
            binding.imgNoteC6.setImageResource(0)
            binding.imgNoteC2.setImageResource(0)
            binding.imgNoteC8.setImageResource(0)
            onColorButtonClick(R.color.pinkNote)
        }

        binding.color8.setOnClickListener {
            binding.imgNoteC8.setImageResource(R.drawable.ic_tick)
            binding.imgNoteC1.setImageResource(0)
            binding.imgNoteC3.setImageResource(0)
            binding.imgNoteC4.setImageResource(0)
            binding.imgNoteC5.setImageResource(0)
            binding.imgNoteC6.setImageResource(0)
            binding.imgNoteC7.setImageResource(0)
            binding.imgNoteC2.setImageResource(0)
            onColorButtonClick(R.color.purpleNote)
        }

        save()

        binding.buttonCancel.setOnClickListener {
            finish()
        }
    }

    private fun updateBackgroundColor() {
        binding.frameAdd.setBackgroundColor(getSelectedColor())
    }

    private fun getSelectedColor(): Int {
        return if (selectedColor != android.R.color.transparent) {
            getColor(selectedColor)
        } else {
            getColor(android.R.color.transparent) // Default transparent color
        }
    }

    private fun save() {
        binding.buttonSave.setOnClickListener {
            val catInput = binding.editTextCategoryTitle
            val cat: String = catInput.text.toString()

            if (cat.isEmpty()) {
                showToast("Please enter a category.")
            } else if (isNoColorSelected()) {
                showToast("Please choose a color for your category.")
            } else {
                val newCategory = TheCategory(cat, getSelectedColor())
                categories.add(newCategory)

                // Save the updated list to the intent
                intent.putExtra("categories", categories)
                intent.putExtra("selectedCategory", cat) // Pass the selected category name

                // Set the result to indicate success
                setResult(Activity.RESULT_OK, intent)

                // Close the AddCategory_ activity
                finish()
            }
        }
    }

    private fun isNoColorSelected(): Boolean {
        return (binding.imgNoteC1.drawable == null &&
                binding.imgNoteC2.drawable == null &&
                binding.imgNoteC3.drawable == null &&
                binding.imgNoteC4.drawable == null &&
                binding.imgNoteC5.drawable == null &&
                binding.imgNoteC6.drawable == null &&
                binding.imgNoteC7.drawable == null &&
                binding.imgNoteC8.drawable == null)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun onColorButtonClick(colorResource: Int) {
        selectedColor = colorResource
        updateBackgroundColor()
    }
}

