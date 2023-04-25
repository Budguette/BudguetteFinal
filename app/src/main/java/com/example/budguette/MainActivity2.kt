package com.example.budguette

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity2 : AppCompatActivity() {

    companion object {
        var fm: FragmentManager? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        fm = supportFragmentManager

        // define your fragments here
        val fragment1: Fragment = Home()
        val fragment2: Fragment = Post()
        val fragment3: Fragment = Budget()
        val fragment4: Fragment = Profile()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.home -> fragment = fragment1
                R.id.library -> fragment = fragment2
                R.id.camera -> fragment = fragment3
                R.id.profile -> fragment = fragment4
            }
            fm!!.beginTransaction().replace(R.id.frame, fragment).commit()
            true
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.home
    }
}