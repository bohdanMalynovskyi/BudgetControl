package com.example.budgetcontrol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.budgetcontrol.enum.Screen

class MainActivity : AppCompatActivity() {

    private val FRAGMENT: String = "FRAGMENT"

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.fragment_container)
    }

    fun navigateToFragment(fragment: Screen){
        when(fragment){
            Screen.BUDGET -> navController.navigate(R.id.budgetFragment)
            Screen.INCOME, Screen.COSTS -> {
                val bundle = Bundle()
                bundle.putSerializable(FRAGMENT, fragment)
                navController.navigate(R.id.incomeCostsFragment, bundle)
            }
            Screen.TARGET -> navController.navigate(R.id.targetFragment)
            Screen.GAUSS_NUMBERS -> navController.navigate(R.id.gaussNumbersFragment)
            Screen.INFORMATION -> navController.navigate(R.id.informationFragment)
        }
    }
}