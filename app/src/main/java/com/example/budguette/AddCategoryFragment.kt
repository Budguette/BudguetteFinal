package com.example.budguette

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts


class AddCategoryFragment : Fragment() {

    lateinit var titleEdittext: EditText
    lateinit var budgetEditText: EditText
    lateinit var imageView: ImageView
    lateinit var btnAdd: Button
    lateinit var databaseHelper: DatabaseHelper
    lateinit var imageUri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_add_category, container, false)

        databaseHelper = context?.let { DatabaseHelper(it) }!!

        titleEdittext = view.findViewById(R.id.titleEdittext)
        budgetEditText = view.findViewById(R.id.bdgetEditText)
        imageView = view.findViewById(R.id.imageView)
        btnAdd = view.findViewById(R.id.btnAdd)

        btnAdd.setOnClickListener {
            addCategory()
        }

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getContent.launch(intent)
        }


        return view
    }

    private fun addCategory() {
        val title: String = titleEdittext.text.toString()
        val budget: String = budgetEditText.text.toString()
        if (title.isEmpty()) {
            Toast.makeText(context, "Add title", Toast.LENGTH_SHORT).show()
        } else if (budget.isEmpty()) {
            Toast.makeText(context, "Add budget", Toast.LENGTH_SHORT).show()
        } else if (imageUri == null) {
            Toast.makeText(context, "Select Icon", Toast.LENGTH_SHORT).show()
        } else {


            // check if same category already exist
            if (databaseHelper.getCategoryByTitle(title) != null) {
                Toast.makeText(context, "This category Already created!", Toast.LENGTH_SHORT).show()
                return;
            }

            var let = getBytes()?.let { Category(0, title, budget.toInt(), it) }
                ?.let { databaseHelper.insertCategory(it) }

            if (let != null) {
                if (let > 0) {
                    Toast.makeText(context, "Added Category", Toast.LENGTH_SHORT).show()
                    imageView.setImageResource(R.drawable.baseline_image_24)
                    titleEdittext.setText("")
                    budgetEditText.setText("")

                } else {
                    Toast.makeText(context, "Category not saved", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getBytes(): ByteArray? {
        val inputStream = requireActivity().contentResolver.openInputStream(imageUri)
        val bytes = inputStream?.readBytes()
        return bytes;
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null && data.data != null) {
                    imageUri = data.data!!
                    imageView.setImageURI(imageUri)

                    // save bytes to database
                }
            }
        }
}