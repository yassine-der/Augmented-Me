package com.example.augmentedme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.augmentedme.Fragment.AddPatchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.opencv.android.OpenCVLoader

class MainActivityhome : AppCompatActivity() {
    private  val addPatchFragment = AddPatchFragment()

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var  buttonNavigator: BottomNavigationView
    lateinit var  floatingActionButton: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_activityhome)
        buttonNavigator = findViewById(R.id.buttonNavigation)
        //val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_graph) as NavHostFragment
        //val navController = navHostFragment.navController
        //buttonNavigator.background = null
        println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")
        println("openCv${OpenCVLoader.initDebug()}")
        println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.patchFragment,R.id.addPatchFragment, R.id.profileFragment)
        )


        setupActionBarWithNavController(navController, appBarConfiguration)

        buttonNavigator.setupWithNavController(navController)
    }
        private fun replaceFragment(fragment: Fragment) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.commit()
        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu0, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.mLogout -> {
                //TODO 5 "Clear the SharedPreferences file and destroy the activity"
                getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().clear().apply()
                finish()
            }
        }

        return   super.onOptionsItemSelected(item)

    }



    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}