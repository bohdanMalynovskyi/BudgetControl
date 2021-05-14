package com.example.budgetcontrol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.budgetcontrol.enum.FragmentName

class MainActivity : AppCompatActivity() {

    private val FRAGMENT: String = "FRAGMENT"

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.fragment_container)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun navigateToFragment(fragment: FragmentName){
        when(fragment){
            FragmentName.BUDGET -> navController.navigate(R.id.budgetFragment)
            FragmentName.INCOME, FragmentName.COSTS -> {
                val bundle = Bundle()
                bundle.putSerializable(FRAGMENT, fragment)
                navController.navigate(R.id.incomeCostsFragment, bundle)
            }
            FragmentName.TARGET -> navController.navigate(R.id.targetFragment)
            FragmentName.GAUSS_NUMBERS -> navController.navigate(R.id.gaussNumbersFragment)
            FragmentName.INFORMATION -> navController.navigate(R.id.informationFragment)
        }
    }
}