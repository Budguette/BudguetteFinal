package com.example.budguette

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Profile : Fragment() {

    private lateinit var nameEditText: EditText
    lateinit var userNameEdittext: EditText
    lateinit var imageView3: ImageView
    lateinit var button2: Button
    lateinit var databaseHelper: DatabaseHelper
    lateinit var imageUri: Uri
    lateinit var recyclerView1: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        databaseHelper = context?.let { DatabaseHelper(it) }!!
        recyclerView1 = view.findViewById(R.id.recyclerView1)
        nameEditText = view.findViewById(R.id.nameEditText)
        userNameEdittext = view.findViewById(R.id.userNameEdittext)
        imageView3 = view.findViewById(R.id.imageView3)
        button2 = view.findViewById(R.id.button2)

        button2.setOnClickListener {
            saveData()
        }

        imageView3.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getContent.launch(intent)
        }


        return view
    }
    private fun loadData() {
        button2
        val allPost = databaseHelper.getAllCategories()
        recyclerView1.layoutManager = LinearLayoutManager(context)
    }


    private fun saveData() {
        val name: String = nameEditText.text.toString()
        val user: String = userNameEdittext.text.toString()
        if (name.isEmpty()) {
            Toast.makeText(context, "Add title", Toast.LENGTH_SHORT).show()
        } else if (user.isEmpty()) {
            Toast.makeText(context, "Add budget", Toast.LENGTH_SHORT).show()
        } else {


            // check if same category already exist
            if (databaseHelper.getCategoryByTitle(name) != null) {
                Toast.makeText(context, "This category Already created!", Toast.LENGTH_SHORT).show()
                return;
            }

            var let = getBytes()?.let { Category(0, name, user.toInt(), it) }
                ?.let { databaseHelper.insertCategory(it) }

            if (let != null) {
                if (let > 0) {
                    Toast.makeText(context, "Added Category", Toast.LENGTH_SHORT).show()
                    imageView3.setImageResource(R.drawable.baseline_image_24)
                    nameEditText.setText("")
                    userNameEdittext.setText("")

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
                    imageView3.setImageURI(imageUri)

                    // save bytes to database
                }
            }
        }
}