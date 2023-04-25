package com.example.budguette

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
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


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class EditCategoryFragment : Fragment() {
    private var id: Int = 0
    private var oldTitle: String? = null
    private var budget: Int? = 0
    private lateinit var icon: ByteArray
    private lateinit var titleEdittext: EditText
    lateinit var budgetEditText: EditText
    lateinit var imageView: ImageView
    lateinit var btnAdd: Button
    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt("id")
            oldTitle = it.getString("title")
            budget = it.getInt("budget")
            icon = it.getByteArray("icon")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_category, container, false)
        databaseHelper = context?.let { DatabaseHelper(it) }!!

        titleEdittext = view.findViewById(R.id.titleEdittext)
        budgetEditText = view.findViewById(R.id.bdgetEditText)
        imageView = view.findViewById(R.id.imageView)
        btnAdd = view.findViewById(R.id.btnAdd)

        assignData()

        btnAdd.setOnClickListener {
            addCategory()
        }

        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getContent.launch(intent)
        }

        return view
    }

    private fun assignData() {
        titleEdittext.setText(oldTitle)
        budgetEditText.setText("$budget")
        val bitmap = BitmapFactory.decodeByteArray(icon, 0, icon.size)
        imageView.setImageBitmap(bitmap)
    }

    private fun addCategory() {
        val title: String = titleEdittext.text.toString()
        val budget: String = budgetEditText.text.toString()
        if (title.isEmpty()) {
            Toast.makeText(context, "Add title", Toast.LENGTH_SHORT).show()
        } else if (budget.isEmpty()) {
            Toast.makeText(context, "Add budget", Toast.LENGTH_SHORT).show()
        } else if (icon == null) {
            Toast.makeText(context, "Select Icon", Toast.LENGTH_SHORT).show()
        } else {
            val updateCategoryTitle = databaseHelper.updateCategoryTitle(
                oldTitle!!,
                Category(0, title, budget.toInt(), icon)
            )
            if (updateCategoryTitle) {
                Toast.makeText(context, "Category Updated!", Toast.LENGTH_SHORT).show()
                MainActivity2.fm?.beginTransaction()?.replace(R.id.frame, Budget())?.commit()
            } else {
                Toast.makeText(context, "Category Not Updated!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun convertIntoByte(imageUri: Uri): ByteArray? {
        val inputStream = requireActivity().contentResolver.openInputStream(imageUri)
        val bytes = inputStream?.readBytes()
        icon = bytes!!
        return bytes
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null && data.data != null) {
                    var imageUri = data.data!!
                    imageView.setImageURI(imageUri)
                    convertIntoByte(imageUri)
                }
            }
        }
}