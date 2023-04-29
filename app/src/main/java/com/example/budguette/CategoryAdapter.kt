package com.example.budguette

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private val categories: List<Category>, private val categoryInstance: Budget) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val colors = arrayOf(
        "#FF5722",
        "#E91E63",
        "#9C27B0",
        "#673AB7",
        "#3F51B5",
        "#2196F3",
        "#03A9F4",
        "#00BCD4",
        "#009688",
        "#4CAF50",
        "#8BC34A",
        "#CDDC39",
        "#FFEB3B",
        "#FFC107",
        "#FF9800",
        "#795548",
        "#9E9E9E",
        "#607D8B"
    )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
        val color = Color.parseColor(colors[position % colors.size])
        holder.itemView.setBackgroundColor(color)


        val bundle = Bundle()
        bundle.putString("title", category.title)
        bundle.putInt("budget", category.budget)
        bundle.putInt("id", category.id)
        bundle.putByteArray("icon", category.icon)


        holder.itemView.setOnClickListener {
            val fragment = ExpensesFragment()
            fragment.arguments = bundle
            MainActivity2.fm?.beginTransaction()?.replace(R.id.frame, fragment)?.commit()
        }

        holder.deleteIv.setOnClickListener {
            categoryInstance.deleteCategory(category)
        }
        holder.editIv.setOnClickListener {
            val fragment = EditCategoryFragment()
            fragment.arguments = bundle
            MainActivity2.fm?.beginTransaction()?.replace(R.id.frame, fragment)?.commit()
        }


    }


    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val icon: ImageView = itemView.findViewById(R.id.icon)
        val editIv: ImageView = itemView.findViewById(R.id.editIv)
        val deleteIv: ImageView = itemView.findViewById(R.id.deleteIv)

        fun bind(category: Category) {
            nameTextView.text = category.title
            val bitmap = BitmapFactory.decodeByteArray(category.icon, 0, category.icon.size)
            icon.setImageBitmap(bitmap)
        }
    }
}