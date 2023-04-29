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
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Post.newInstance] factory method to
 * create an instance of this fragment.
 */

class Post : Fragment() {

    private lateinit var titleEditText4: EditText
    lateinit var catEdittext4: EditText
    lateinit var button3: Button
    lateinit var databaseHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_post, container, false)

        databaseHelper = context?.let { DatabaseHelper(it) }!!

        titleEditText4 = view.findViewById(R.id.titleEditText4)
        catEdittext4 = view.findViewById(R.id.catEdittext4)
        button3 = view.findViewById(R.id.button3)

        button3.setOnClickListener {
            addCategory()
        }


        return view
    }

    private fun addCategory() {
        val title: String = titleEditText4.text.toString()
        val category: String = catEdittext4.text.toString()
        if (title.isEmpty()) {
            Toast.makeText(context, "Add title", Toast.LENGTH_SHORT).show()
        } else if (category.isEmpty()) {
            Toast.makeText(context, "Add budget", Toast.LENGTH_SHORT).show()
        } else {



                }
            }
        }

