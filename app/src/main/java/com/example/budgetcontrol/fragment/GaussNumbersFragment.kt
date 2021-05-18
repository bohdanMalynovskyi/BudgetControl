package com.example.budgetcontrol.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.budgetcontrol.MainActivity
import com.example.budgetcontrol.R
import com.example.budgetcontrol.db.BudgetControlDB
import com.example.budgetcontrol.db.model.Target
import com.example.budgetcontrol.dialog.ConfirmationDialog
import com.example.budgetcontrol.enum.FragmentType
import com.example.budgetcontrol.view.GaussNumberView
import kotlinx.android.synthetic.main.gauss_numbers_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GaussNumbersFragment : Fragment() {

    private var changedGaussNumbersMap = HashMap<Int, Boolean>()
    private var changedCollectedAmount = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.gauss_numbers_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        fillGaussNumbersLayout()
        setCollectedAmount()
        gaussNumbersSeekBar.isEnabled = false
        gaussNumbersFragmentConfirmButton.setOnClickListener(this::handleConfirmButtonClick)
    }

    private fun setCollectedAmount() {
        GlobalScope.launch {
            val targetCollectedAmount = BudgetControlDB.getInstance(requireContext())
                    .targetDao()
                    .getTargetCollectedAmount(Target.GAUSS_NUMBER_TARGET_ID)
            changedCollectedAmount = targetCollectedAmount

            activity?.runOnUiThread {
                collectedAmountTextView.text = targetCollectedAmount.toString()
                gaussNumbersSeekBar.progress = targetCollectedAmount
            }
        }
    }

    private fun fillGaussNumbersLayout() {
        gaussNumbersFlexBoxLayout.removeAllViews()

        GlobalScope.launch {
            val gaussNumbersStatusesList = BudgetControlDB.getInstance(requireContext())
                    .gaussNumberDao()
                    .getStatuses()

            activity?.runOnUiThread {
                for (number in 1..100) {
                    val gaussNumber = GaussNumberView(context, number)
                    val index = number - 1
                    val isCollected = gaussNumbersStatusesList[index]
                    gaussNumber.setStatus(isCollected == 1)

                    gaussNumber.apply {
                        setOnClickListener(this@GaussNumbersFragment::handleGaussNumberViewClick)
                    }
                    gaussNumbersFlexBoxLayout.addView(gaussNumber)
                }
            }
        }
    }

    private fun handleGaussNumberViewClick(view: View) {
        val gaussNumber = view as GaussNumberView

        gaussNumber.apply {
            if (isCollected()) {
                val dialog = ConfirmationDialog(context, getString(R.string.you_want_to_cancel_contribution))
                dialog.apply {
                    confirmButton.setOnClickListener {
                        changeGaussNumberStatus(gaussNumber)
                        dismiss()
                    }
                    show()
                }
            } else {
                changeGaussNumberStatus(gaussNumber)
            }
        }
    }

    private fun changeGaussNumberStatus(gaussNumber: GaussNumberView) {
        gaussNumber.apply {
            val isCollected = !isCollected()
            setStatus(isCollected)
            changedGaussNumbersMap[getValue()] = isCollected

            when (isCollected) {
                true -> changedCollectedAmount += getValue()
                false -> changedCollectedAmount -= getValue()
            }
            gaussNumbersSeekBar.progress = changedCollectedAmount
            collectedAmountTextView.text = changedCollectedAmount.toString()
        }
    }

    private fun handleConfirmButtonClick(view: View) {
        val gaussNumberDao = BudgetControlDB.getInstance(requireContext()).gaussNumberDao()

        GlobalScope.launch {
            changedGaussNumbersMap.forEach {
                val value = it.key
                val isCollected = it.value
                gaussNumberDao.updateStatus(value, if (isCollected) 1 else 0)
            }
            BudgetControlDB.getInstance(requireContext())
                    .targetDao()
                    .updateTargetCollectedAmount(Target.GAUSS_NUMBER_TARGET_ID, changedCollectedAmount)
        }

        (activity as MainActivity).navigateToFragment(FragmentType.BUDGET)
    }
}