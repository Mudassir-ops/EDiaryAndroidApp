package com.example.neweasydairy.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.neweasydairy.fragments.pinFragment.PinFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var isPasswordFragmentLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        Log.e("CurrentFragment", "onResume: ")
        logCurrentFragment()
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
//        if (currentFragment !is PinFragment) {
//            loadPinFragment()
//        }

        val navController = findNavController(R.id.nav_host_fragment)
        if (navController.currentDestination?.id == R.id.mainFragment
            || navController.currentDestination?.id == R.id.createNotesFragment
            || navController.currentDestination?.id == R.id.libraryFragment
            || navController.currentDestination?.id == R.id.calendarFragment
            || navController.currentDestination?.id == R.id.tagsFragment
        ) {
            navController.navigate(R.id.pinFragment)
        }


    }

    override fun onPause() {
        super.onPause()
        Log.e("CurrentFragment", "onPause: ")
        logCurrentFragment()
    }


    private fun logCurrentFragment() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)

        if (navHostFragment != null && navHostFragment is androidx.navigation.fragment.NavHostFragment) {
            val currentFragment = navHostFragment.childFragmentManager.primaryNavigationFragment
            if (currentFragment != null) {
                Log.e(
                    "CurrentFragment",
                    "ID: ${currentFragment.id}, Name: ${currentFragment::class.java.simpleName}"
                )
            } else {
                Log.e("CurrentFragment", "No fragment found in NavHostFragment!")
            }
        } else {
            Log.e("CurrentFragment", "NavHostFragment not found!")
        }
    }

    private fun loadPinFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, PinFragment())
            .commit()
    }

}