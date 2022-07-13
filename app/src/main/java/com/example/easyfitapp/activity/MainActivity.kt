package com.example.easyfitapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.easyfitapp.R
import com.example.easyfitapp.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val homeFragment = WeightFragment()
    private val activityFragment = ActivityFragment()
    private val videoFragment = VideoFragment()
    private val profileFragment = ProfileFragment()
    lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(homeFragment)

        bottomNav = findViewById(R.id.bottomNavView) as BottomNavigationView
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(homeFragment)
                R.id.activity -> replaceFragment(activityFragment)
                R.id.video -> replaceFragment(videoFragment)
                R.id.account -> replaceFragment(profileFragment)
            }
            true
        }
    }
    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_fragment_container, fragment)
        transaction.commit()
    }
}