package com.example.arfinance.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.arfinance.R
import com.example.arfinance.databinding.ActivityMainBinding
import com.example.arfinance.util.interfaces.OpenFullScreenListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OpenFullScreenListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        setupActionBarWithNavController(navController)
    }


    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.nav_host_fragment).navigateUp()

    override fun onScreenOpen() {
        /*binding.addTransactionFab.visibility = View.GONE
        binding.bottomAppBar.visibility = View.GONE*/
    }

    override fun onScreenClose() {
        /* binding.addTransactionFab.visibility = View.VISIBLE
         binding.bottomAppBar.visibility = View.VISIBLE*/
    }

}