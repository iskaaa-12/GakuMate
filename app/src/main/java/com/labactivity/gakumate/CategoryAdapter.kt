package com.labactivity.gakumate

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private var originalDataList: ArrayList<TheCategory>) : RecyclerView.Adapter<CategoryAdapter.CatViewHolder>() {

    private var dataList: ArrayList<TheCategory> = ArrayList(originalDataList)
    fun filter(query: String) {
        dataList.clear()

        if (query.isEmpty()) {
            dataList.addAll(originalDataList)
        } else {
            val filteredList = originalDataList.filter {
                it.category.toLowerCase().contains(query.toLowerCase())
            }
            dataList.addAll(filteredList)
        }

        notifyDataSetChanged()
    }

    fun updateData(newData: ArrayList<TheCategory>) {
        originalDataList.clear()
        originalDataList.addAll(newData)

        dataList.clear()
        dataList.addAll(newData)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_list, parent, false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val currentItem = dataList[position]

        // Bind data to views in your ViewHolder
        holder.bind(currentItem)

        // Set click listener for the item
        holder.itemView.setOnClickListener {
            val intentMain = Intent(holder.itemView.context, TodoList::class.java)
            intentMain.putExtra(
                "categoryName",
                currentItem.category
            ) // Use the correct property for the category name
            holder.itemView.context.startActivity(intentMain)
        }
    }


    override fun getItemCount(): Int {
        return dataList.size
    }


    inner class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ViewHolder implementation, if needed
        fun bind(category: TheCategory) {
            // Assuming you have a TextView in your category_list layout with ID textViewCategoryName
            val categoryNameTextView = itemView.findViewById<TextView>(R.id.categoryTitle)
            categoryNameTextView.text = category.category // Change 'name' to the actual property of TheCategory containing the name
        }
    }
}