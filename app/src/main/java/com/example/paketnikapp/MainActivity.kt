package com.example.paketnikapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.paketnikapp.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var app: MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        app = application as MyApplication

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController)

        binding.bottomNav.setupWithNavController(navController)

        // Ali smo se že prijavili
        if(app.getUser().username != "") {
            Timber.tag("User info").d(app.getUser().toString())
            navController.navigate(R.id.homeFragment)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.fragment_first_page || destination.id == R.id.fragment_login
                || destination.id == R.id.fragment_register || destination.id == R.id.loginCameraFragment) {
                binding.bottomNav.visibility = View.GONE
            } else {
                binding.bottomNav.visibility = View.VISIBLE

                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                supportActionBar?.setDisplayShowHomeEnabled(false)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun getApp(): MyApplication {
        return app
    }
}