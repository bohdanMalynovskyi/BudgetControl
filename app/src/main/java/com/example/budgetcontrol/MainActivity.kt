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

    companion object{
        const val BUDGET_COMPONENT: String = "BUDGET_COMPONENT"
        const val BUDGET_TITLE: String = "Бюджет"
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
            FragmentType.BUDGET -> {
                navController.navigate(R.id.budgetFragment)
                setToolbarTitle(getString(R.string.budget))
            }
            FragmentType.INCOME -> {
                navigateToIncomeCostsFragment(BudgetComponent.INCOME)
                setToolbarTitle(getString(R.string.income))
            }
            FragmentType.COSTS -> {
                navigateToIncomeCostsFragment(BudgetComponent.COSTS)
                setToolbarTitle(getString(R.string.costs))
            }
            FragmentType.TARGET -> {
                navController.navigate(R.id.targetFragment)
                setToolbarTitle(getString(R.string.target))
            }
            FragmentType.GAUSS_NUMBERS -> {
                navController.navigate(R.id.gaussNumbersFragment)
                setToolbarTitle(getString(R.string.fifty_fifty))
            }
            FragmentType.INFORMATION -> {
                navController.navigate(R.id.informationFragment)
                setToolbarTitle(getString(R.string.information))
            }
        }
    }

    private fun setupToolbar() {
        this.toolbar = budgetControlToolbar
        setToolbarTitle(BUDGET_TITLE)
        setSupportActionBar(toolbar)
    }

    private fun setToolbarTitle(title: String) {
        toolbar.title = title
    }

    private fun navigateToIncomeCostsFragment(budgetComponent: BudgetComponent) {
        val bundle = Bundle()
        bundle.putSerializable(BUDGET_COMPONENT, budgetComponent)
        navController.navigate(R.id.incomeCostsFragment, bundle)
    }
}