package com.mabnets.statussaverquick

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.InterstitialAd
import com.mabnets.statussaverquick.Utilz.showPermissionRequestExplanation
import com.mabnets.statussaverquick.databinding.ActivityMainBinding
import com.mabnets.statussaverquick.viewmodels.Mainviewmodel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private  lateinit var navController: NavController
    private  val viewmodel by viewModels<Mainviewmodel>()
    private var showCount = 0;
    private var mInterstitialAd: InterstitialAd? = null

    private val requestMultiplePermissions =registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.entries.forEach {
            Log.d(TAG, "${it.key} = ${it.value}")
        }
        if (permissions[READ_EXTERNAL_STORAGE] == true && permissions[WRITE_EXTERNAL_STORAGE] == true) {
            Log.d(TAG, "Permission granted")
        } else {
            requestStoragePermission()
        }
        /* if (!granted) {
             Toast.makeText(this, "Storage Permission NOT Granted", Toast.LENGTH_SHORT).show()
             requestStoragePermission()
         }*/

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
       navController = findNavController(R.id.nav_host_fragment_content_index)
       appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.vimages,R.id.vvideos,R.id.vdownloads2
            )
        )
        toolbar.setupWithNavController(navController,appBarConfiguration)
        init(savedInstanceState)
        /*navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.launcher) {
                toolbar.visibility = View.GONE
            }else{
                toolbar.visibility = View.VISIBLE
            }
        }*/
        requestStoragePermission()


    }
    private fun init(savedInstanceState: Bundle?) {
        binding.bottomNavigationView.setupWithNavController(navController)
    }
    //asking for permission
    private fun requestStoragePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    WRITE_EXTERNAL_STORAGE,
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // The permission is granted
                    // you can go with the flow that requires permission here
                }
                shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE) -> {
                    // This case means user previously denied the permission
                    // So here we can display an explanation to the user
                    // That why exactly we need this permission
                    showPermissionRequestExplanation(
                        getString(R.string.write_storage),
                        getString(R.string.permission_request)
                    ) { requestMultiplePermissions .launch(arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)) }
                }
                else -> {
                    // Everything is fine you can simply request the permission
                    requestMultiplePermissions .launch(arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE))
                }
            }
        }
    }

}