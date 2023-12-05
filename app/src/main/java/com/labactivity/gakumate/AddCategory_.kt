package com.labactivity.gakumate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.labactivity.gakumate.databinding.ActivityAddCategoryBinding


class AddCategory_ : AppCompatActivity() {
    private lateinit var binding: ActivityAddCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val catInput = binding.editTextCategoryTitle
        val cat: String = catInput.text.toString()




        binding.color1.setOnClickListener(){
            binding.imgNoteC1.setImageResource(R.drawable.ic_tick)
            binding.imgNoteC2.setImageResource(0)
            binding.imgNoteC3.setImageResource(0)
            binding.imgNoteC4.setImageResource(0)
            binding.imgNoteC5.setImageResource(0)
            binding.imgNoteC6.setImageResource(0)
            binding.imgNoteC7.setImageResource(0)
            binding.imgNoteC8.setImageResource(0)

        }

        binding.color2.setOnClickListener(){
            binding.imgNoteC2.setImageResource(R.drawable.ic_tick)
            binding.imgNoteC1.setImageResource(0)
            binding.imgNoteC3.setImageResource(0)
            binding.imgNoteC4.setImageResource(0)
            binding.imgNoteC5.setImageResource(0)
            binding.imgNoteC6.setImageResource(0)
            binding.imgNoteC7.setImageResource(0)
            binding.imgNoteC8.setImageResource(0)
        }

        binding.color3.setOnClickListener(){
            binding.imgNoteC3.setImageResource(R.drawable.ic_tick)
            binding.imgNoteC1.setImageResource(0)
            binding.imgNoteC2.setImageResource(0)
            binding.imgNoteC4.setImageResource(0)
            binding.imgNoteC5.setImageResource(0)
            binding.imgNoteC6.setImageResource(0)
            binding.imgNoteC7.setImageResource(0)
            binding.imgNoteC8.setImageResource(0)
        }

        binding.color4.setOnClickListener(){
            binding.imgNoteC4.setImageResource(R.drawable.ic_tick)
            binding.imgNoteC1.setImageResource(0)
            binding.imgNoteC3.setImageResource(0)
            binding.imgNoteC2.setImageResource(0)
            binding.imgNoteC5.setImageResource(0)
            binding.imgNoteC6.setImageResource(0)
            binding.imgNoteC7.setImageResource(0)
            binding.imgNoteC8.setImageResource(0)
        }

        binding.color5.setOnClickListener(){
            binding.imgNoteC5.setImageResource(R.drawable.ic_tick)
            binding.imgNoteC1.setImageResource(0)
            binding.imgNoteC3.setImageResource(0)
            binding.imgNoteC4.setImageResource(0)
            binding.imgNoteC2.setImageResource(0)
            binding.imgNoteC6.setImageResource(0)
            binding.imgNoteC7.setImageResource(0)
            binding.imgNoteC8.setImageResource(0)
        }

        binding.color6.setOnClickListener(){
            binding.imgNoteC6.setImageResource(R.drawable.ic_tick)
            binding.imgNoteC1.setImageResource(0)
            binding.imgNoteC3.setImageResource(0)
            binding.imgNoteC4.setImageResource(0)
            binding.imgNoteC5.setImageResource(0)
            binding.imgNoteC2.setImageResource(0)
            binding.imgNoteC7.setImageResource(0)
            binding.imgNoteC8.setImageResource(0)
        }

        binding.color7.setOnClickListener(){
            binding.imgNoteC7.setImageResource(R.drawable.ic_tick)
            binding.imgNoteC1.setImageResource(0)
            binding.imgNoteC3.setImageResource(0)
            binding.imgNoteC4.setImageResource(0)
            binding.imgNoteC5.setImageResource(0)
            binding.imgNoteC6.setImageResource(0)
            binding.imgNoteC2.setImageResource(0)
            binding.imgNoteC8.setImageResource(0)
        }

        binding.color8.setOnClickListener(){
            binding.imgNoteC8.setImageResource(R.drawable.ic_tick)
            binding.imgNoteC1.setImageResource(0)
            binding.imgNoteC3.setImageResource(0)
            binding.imgNoteC4.setImageResource(0)
            binding.imgNoteC5.setImageResource(0)
            binding.imgNoteC6.setImageResource(0)
            binding.imgNoteC7.setImageResource(0)
            binding.imgNoteC2.setImageResource(0)
        }
        save()


    }
    fun save(){
        val catInput = binding.editTextCategoryTitle
        binding.buttonSave.setOnClickListener(){
            val cat: String = catInput.text.toString()
            if (cat.isEmpty()){
                Toast.makeText(this, "Please enter a category.", Toast.LENGTH_SHORT).show()
            }else if (isNoColorSelected()) {
                Toast.makeText(this, "Please choose a color for your category.", Toast.LENGTH_SHORT).show()
            }  else {



            }
        }
}
    private fun isNoColorSelected(): Boolean {
        // Check if all FrameLayouts have the ImageView unset (no color chosen)
        return (binding.imgNoteC1.drawable == null &&
                binding.imgNoteC2.drawable == null &&
                binding.imgNoteC3.drawable == null &&
                binding.imgNoteC4.drawable == null &&
                binding.imgNoteC5.drawable == null &&
                binding.imgNoteC6.drawable == null &&
                binding.imgNoteC7.drawable == null &&
                binding.imgNoteC8.drawable == null)
    }

    private fun getSelectedColor(): Int {
        // Check which FrameLayout has the image set and return the corresponding color

        when {
            binding.imgNoteC1.drawable != null -> return getColor(R.color.blueNote)
            binding.imgNoteC2.drawable != null -> return getColor(R.color.yellowNote)
            binding.imgNoteC3.drawable != null -> return getColor(R.color.greenNote)
            binding.imgNoteC4.drawable != null -> return getColor(R.color.indigoNote)
            binding.imgNoteC5.drawable != null -> return getColor(R.color.redNote)
            binding.imgNoteC6.drawable != null -> return getColor(R.color.orangeNote)
            binding.imgNoteC7.drawable != null -> return getColor(R.color.pinkNote)
            binding.imgNoteC8.drawable != null -> return getColor(R.color.purpleNote)
            else -> return getColor(android.R.color.transparent) // Default transparent color
        }
    }

}


