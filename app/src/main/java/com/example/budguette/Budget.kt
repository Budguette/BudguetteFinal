package com.example.budguette

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class Budget : Fragment(), CategoryInstance {
    lateinit var addFab: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var databaseHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_budget, container, false)
        addFab = view.findViewById(R.id.addFab);

        recyclerView = view.findViewById(R.id.recyclerview)
        databaseHelper = context?.let { DatabaseHelper(it) }!!
        addFab.setOnClickListener {
            MainActivity2.fm?.beginTransaction()?.replace(R.id.frame, AddCategoryFragment())
                ?.commit();
        }

        loadItems();
        return view
    }

    private fun loadItems() {
        val allCategories = databaseHelper.getAllCategories()

        val categoryAdapter = CategoryAdapter(allCategories, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = categoryAdapter
    }

    override fun onResume() {
        super.onResume()
    }

    override fun editCategory(category: Category) {

        MainActivity2.fm?.beginTransaction()?.replace(R.id.frame, EditCategoryFragment())
            ?.commit();

    }

    override fun deleteCategory(category: Category) {

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this item?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Delete") { dialog, which ->
                val res = databaseHelper.deleteCategoryByTitle(category.title)
                if (res) {
                    Toast.makeText(context, "Category Deleted Successfully!", Toast.LENGTH_SHORT)
                        .show()
                    loadItems()
                } else {
                    Toast.makeText(context, "Category not Deleted!", Toast.LENGTH_SHORT).show()
                }

            }
            .setNegativeButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
    }
}