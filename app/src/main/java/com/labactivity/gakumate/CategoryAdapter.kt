package com.labactivity.gakumate

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private val dataList: List<TheCategory>) : RecyclerView.Adapter<CategoryAdapter.CatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_list, parent, false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val currentItem = dataList[position]

        // Bind data to views in your ViewHolder here

        // Set click listener for the item
        holder.itemView.setOnClickListener {
            val intentMain = Intent(holder.itemView.context, MainActivity::class.java)
            intentMain.putExtra("category", currentItem.category)
            holder.itemView.context.startActivity(intentMain)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    inner class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ViewHolder implementation, if needed
    }

}