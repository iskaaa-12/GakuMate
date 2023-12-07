package com.labactivity.gakumate

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private var dataList: ArrayList<TheCategory>) : RecyclerView.Adapter<CategoryAdapter.CatViewHolder>() {


    fun filter(query: String) {
        val filteredList = ArrayList<TheCategory>()
        for (category in dataList) {
            if (category.category.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(category)
            }
        }
        dataList = filteredList
        notifyDataSetChanged()
    }

    fun updateData(newData: ArrayList<TheCategory>) {
        dataList = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_list, parent, false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val currentItem = dataList[position]

        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            val intentMain = Intent(holder.itemView.context, TodoList::class.java)
            intentMain.putExtra(
                "categoryName",
                currentItem.category
            )
            holder.itemView.context.startActivity(intentMain)
        }
    }


    override fun getItemCount(): Int {
        return dataList.size
    }


    inner class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(category: TheCategory) {
            val categoryNameTextView = itemView.findViewById<TextView>(R.id.categoryTitle)
            categoryNameTextView.text = category.category
        }
        init {
            val editButton = itemView.findViewById<Button>(R.id.editCat)
            val deleteButton = itemView.findViewById<Button>(R.id.delCat)

            editButton.setOnClickListener {
                val category = dataList[adapterPosition]
                editCategory(category)

                // Show a Toast message
            }

            deleteButton.setOnClickListener {
                // Handle delete button click
                val category = dataList[adapterPosition]
                deleteCategory(category)

                // Show a Toast message
            }
        }

        private fun editCategory(category: TheCategory) {
            // Save the old category title before it's edited
            val oldCategoryTitle = category.category

            // Create an EditText
            val categoryNameEditText = EditText(itemView.context)
            categoryNameEditText.setText(category.category)
            categoryNameEditText.imeOptions = EditorInfo.IME_ACTION_DONE
            categoryNameEditText.setSingleLine()
            categoryNameEditText.gravity = Gravity.CENTER
            categoryNameEditText.textSize = 20f  // Set the text size
            categoryNameEditText.typeface = ResourcesCompat.getFont(itemView.context, R.font.paprika)  // Set the font

            // Create a dialog
            val dialog = AlertDialog.Builder(itemView.context)
                .setTitle("Edit Category")
                .setMessage("Please enter the new category title:")
                .setView(categoryNameEditText)
                .setPositiveButton("OK", null)  // Set to null for now
                .setNegativeButton("Cancel", null)
                .setCancelable(false)  // Prevent the dialog from being dismissed when the user presses outside of it
                .create()

            dialog.setOnShowListener {
                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setOnClickListener {
                    // Get the new category title
                    val newCategoryTitle = categoryNameEditText.text.toString()

                    // Update the category title in your data list
                    category.category = newCategoryTitle
                    notifyDataSetChanged()

                    // Update the tasks associated with this category
                    val tasksSharedPreferencesManager = TasksSharedPreferencesManager(itemView.context, oldCategoryTitle)
                    val tasks = tasksSharedPreferencesManager.getTasks()
                    tasksSharedPreferencesManager.saveTasks(ArrayList())  // Clear the old tasks

                    val newTasksSharedPreferencesManager = TasksSharedPreferencesManager(itemView.context, newCategoryTitle)
                    newTasksSharedPreferencesManager.saveTasks(tasks)  // Save the tasks under the new category title

                    // Save the updated categories to SharedPreferences
                    val databaseHelper = DatabaseHelper(itemView.context)
                    databaseHelper.saveCategories(dataList)

                    Toast.makeText(itemView.context, "Edited Successfully!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }

            dialog.show()

            // Set an OnEditorActionListener to handle the event when the user presses enter
            categoryNameEditText.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Get the new category title
                    val newCategoryTitle = v.text.toString()

                    // Update the category title in your data list
                    category.category = newCategoryTitle
                    notifyDataSetChanged()

                    // Update the tasks associated with this category
                    val tasksSharedPreferencesManager = TasksSharedPreferencesManager(itemView.context, oldCategoryTitle)
                    val tasks = tasksSharedPreferencesManager.getTasks()
                    tasksSharedPreferencesManager.saveTasks(ArrayList())  // Clear the old tasks

                    val newTasksSharedPreferencesManager = TasksSharedPreferencesManager(itemView.context, newCategoryTitle)
                    newTasksSharedPreferencesManager.saveTasks(tasks)  // Save the tasks under the new category title

                    true
                } else {
                    false
                }
            }
        }



        private fun deleteCategory(category: TheCategory) {
            // Implement your delete functionality here
            // For example, you might want to show a dialog to confirm the deletion
            AlertDialog.Builder(itemView.context)
                .setTitle("Delete Category")
                .setMessage("Are you sure you want to delete this category?")
                .setPositiveButton("Yes") { _, _ ->
                    // Delete the category
                    dataList.remove(category)
                    notifyDataSetChanged()
                    Toast.makeText(itemView.context, "Deleted Successfully!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }


}