package com.example.budgetcontrol.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.example.budgetcontrol.MainActivity
import com.example.budgetcontrol.R
import com.example.budgetcontrol.db.BudgetControlDB
import com.example.budgetcontrol.db.model.Target
import com.example.budgetcontrol.dialog.ConfirmationDialog
import com.example.budgetcontrol.enum.FragmentType
import com.example.budgetcontrol.view.GaussNumberView
import kotlinx.android.synthetic.main.gauss_number_info_dialog.*
import kotlinx.android.synthetic.main.gauss_numbers_fragment.*
import kotlinx.android.synthetic.main.record_dialog.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GaussNumbersFragment : Fragment() {

    companion object {
        private const val IS_NOT_COLLECTED = 0
        private const val IS_COLLECTED = 1
    }

    private var changedGaussNumbersMap = HashMap<Int, Boolean>()
    private var changedCollectedAmount = 0F

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.gauss_numbers_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setData()
        (activity as MainActivity).setToolbarTitle(getString(R.string.fifty_fifty))

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        menu.forEach { item ->
            if (item.itemId == R.id.toolbarMenuItemInfo) {
                item.isVisible = true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.toolbarMenuItemInfo) {
            handleInfoButtonClick()
        }
        return true
    }

    private fun handleInfoButtonClick() {
        val dialog = Dialog(requireContext())
        dialog.apply {
            val dialogView = layoutInflater.inflate(R.layout.gauss_number_info_dialog, null)
            setContentView(dialogView)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            gaussNumberInfoDialogCloseButton.setOnClickListener {
                dismiss()
            }
            show()
        }
    }

    private fun setupViews() {
        gaussNumbersSeekBar.isEnabled = false
        gaussNumbersFragmentConfirmButton.setOnClickListener(this::handleConfirmButtonClick)
    }

    private fun setData() {
        fillGaussNumbersLayout()
        setCollectedAmount()
    }

    private fun setCollectedAmount() {
        GlobalScope.launch {
            val targetCollectedAmount = BudgetControlDB.getInstance(requireContext())
                    .targetDao()
                    .getCollectedAmount(Target.GAUSS_NUMBER_TARGET_ID)
            changedCollectedAmount = targetCollectedAmount

            activity?.runOnUiThread {
                collectedAmountTextView.text = targetCollectedAmount.toInt().toString()
                gaussNumbersSeekBar.progress = targetCollectedAmount.toInt()

                if (isTargetAchieved(targetCollectedAmount)) {
                    handleAmountCollected()
                }
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
                    gaussNumber.setStatus(isCollected == IS_COLLECTED)

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
            gaussNumbersSeekBar.progress = changedCollectedAmount.toInt()
            collectedAmountTextView.text = changedCollectedAmount.toInt().toString()
        }
    }

    private fun handleConfirmButtonClick(view: View) {
        GlobalScope.launch {
            changedGaussNumbersMap.forEach {
                val value = it.key
                val isCollected = it.value
                BudgetControlDB.getInstance(requireContext())
                        .gaussNumberDao()
                        .updateStatus(value, if (isCollected) IS_COLLECTED else IS_NOT_COLLECTED)
            }
            BudgetControlDB.getInstance(requireContext())
                    .targetDao()
                    .updateCollectedAmount(Target.GAUSS_NUMBER_TARGET_ID, changedCollectedAmount)
        }
        if (isTargetAchieved(changedCollectedAmount)) {
            handleAmountCollected()
        } else {
            (activity as MainActivity).navigateToFragment(FragmentType.BUDGET)
        }

    }

    private fun handleAmountCollected() {
        val dialog = ConfirmationDialog(
                requireContext(),
                getString(R.string.gauss_numbers_are_collected)
        )
        dialog.apply {
            confirmButton.setOnClickListener {
                startCollectingAgain()
                dismiss()
            }
            show()
        }
    }

    private fun startCollectingAgain() {
        val db = BudgetControlDB.getInstance(requireContext())
        GlobalScope.launch {
            for (number in 1..100) {
                db.gaussNumberDao().updateStatus(number, IS_NOT_COLLECTED)
            }
            val target = Target(
                    Target.GAUSS_NUMBER_TARGET_ID,
                    Target.GAUSS_NUMBER_TARGET_DESCRIPTION,
                    Target.GAUSS_NUMBER_TARGET_AMOUNT,
                    0
            )
            db.targetDao().insert(target)
            activity?.runOnUiThread {
                setData()
            }
        }
    }

    private fun isTargetAchieved(collectedAmount: Float): Boolean {
        return collectedAmount >= Target.GAUSS_NUMBER_TARGET_AMOUNT
    }
}