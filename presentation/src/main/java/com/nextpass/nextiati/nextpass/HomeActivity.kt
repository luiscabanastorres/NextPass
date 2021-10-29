package com.nextpass.nextiati.nextpass

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationBarView
import com.nextpass.nextiati.nextpass.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navigation.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when(item.itemId){
                R.id.item1->{
                    popBackStackFragment(navController)
                    navController.navigate(R.id.navigationAccess)
                    return@OnItemSelectedListener true
                }
                R.id.item2->{
                    popBackStackFragment(navController)
                    navController.navigate(R.id.navigationSuggestions)
                    return@OnItemSelectedListener true
                }
                R.id.item3->{
                    popBackStackFragment(navController)
                    navController.navigate(R.id.navigationProfile)
                    return@OnItemSelectedListener true
                }
                else -> return@OnItemSelectedListener true
            }
        })

    }

    private fun popBackStackFragment(navController:NavController) {
        navController.popBackStack(R.id.navigationAccess, true)
        navController.popBackStack(R.id.navigationSuggestions, true)
        navController.popBackStack(R.id.navigationProfile, true)
    }

}