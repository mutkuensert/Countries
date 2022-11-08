package com.mutkuensert.countries.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.mutkuensert.countries.R
import com.mutkuensert.countries.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigation()
    }

    private fun setNavigation(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    navController.navigate(R.id.homePageFragment)
                    true
                }

                R.id.saved -> {
                    navController.navigate(R.id.savedFragment)
                    true
                }
                else -> { true }
            }
        }
    }

}