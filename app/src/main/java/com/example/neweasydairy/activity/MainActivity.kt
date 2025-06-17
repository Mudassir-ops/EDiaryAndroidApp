package com.example.neweasydairy.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.easydiaryandjournalwithlock.R
import com.example.easydiaryandjournalwithlock.databinding.ActivityMainBinding
import com.example.neweasydairy.data.UpdateState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.content.edit
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val updateViewModel: UpdateViewModel by viewModels()
    private fun isBatteryOptimizationEnabled(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return !powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }

    @SuppressLint("BatteryLife")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        setContentView(binding.root)
        if (isBatteryOptimizationEnabled(this)) {
            Log.d("BatteryCheck", "Battery optimization is ENABLED for this app.")
        } else {
            val intent =
                Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = Uri.parse("package:${packageName}")
                }
            startActivity(intent)
            Log.d("BatteryCheck", "Battery optimization is DISABLED for this app.")
        }
        /**
         * In App Update Check For Each Time on Start
         **/

        updateViewModel.init(this)
        updateViewModel.checkForUpdates()

        lifecycleScope.launch {
            updateViewModel.updateState.collect {
                when (it) {
                    is UpdateState.Downloaded -> {
                        showRestartSnackbar()
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateViewModel.checkDownloadedOnResume()
        Log.e("CurrentFragment", "onResume: ")
        logCurrentFragment()
        val prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val skipPin = prefs.getBoolean("skipPinOnce", false)
        val isComingFromCamera = prefs.getBoolean("isComingFromCamera",false)

        if (skipPin) {
            prefs.edit() { putBoolean("skipPinOnce", false) }
            return
        }
        if (isComingFromCamera) {
            prefs.edit() { putBoolean("isComingFromCamera", false) }
            return
        }

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
                Log.e("CurrentFragment",
                    "ID: ${currentFragment.id}, Name: ${currentFragment::class.java.simpleName}")
            } else {
                Log.e("CurrentFragment", "No fragment found in NavHostFragment!")
            }
        } else {
            Log.e("CurrentFragment", "NavHostFragment not found!")
        }
    }

    private fun showRestartSnackbar() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Update ready",
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction("Restart") {
                updateViewModel.completeUpdate()
            }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        updateViewModel.unregisterListener()
    }


}