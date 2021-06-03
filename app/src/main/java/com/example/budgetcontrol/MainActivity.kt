package com.example.budgetcontrol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.budgetcontrol.enum.BudgetComponent
import com.example.budgetcontrol.enum.FragmentType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //todo add progress view while fetching data

    companion object{
        const val BUDGET_COMPONENT: String = "BUDGET_COMPONENT"
    }

    private lateinit var navController: NavController
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()
        navController = Navigation.findNavController(this, R.id.fragment_container)
    }

    fun navigateToFragment(fragment: FragmentType) {
        when (fragment) {
            FragmentType.BUDGET -> navController.navigate(R.id.budgetFragment)
            FragmentType.INCOME -> navigateToIncomeCostsFragment(BudgetComponent.INCOME)
            FragmentType.COSTS -> navigateToIncomeCostsFragment(BudgetComponent.COSTS)
            FragmentType.TARGET -> navController.navigate(R.id.targetFragment)
            FragmentType.GAUSS_NUMBERS -> navController.navigate(R.id.gaussNumbersFragment)
            FragmentType.INFORMATION -> navController.navigate(R.id.informationFragment)
        }
    }

    fun setToolbarTitle(title: String) {
        toolbar.title = title
    }

    private fun setupToolbar() {
        this.toolbar = budgetControlToolbar
        setToolbarTitle(getString(R.string.budget))
        setSupportActionBar(toolbar)
    }

    private fun navigateToIncomeCostsFragment(budgetComponent: BudgetComponent) {
        val bundle = Bundle()
        bundle.putSerializable(BUDGET_COMPONENT, budgetComponent)
        navController.navigate(R.id.incomeCostsFragment, bundle)
    }
}