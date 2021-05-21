package com.example.budgetcontrol.fragment

import android.os.Bundle
import android.view.*
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.example.budgetcontrol.MainActivity
import com.example.budgetcontrol.R
import com.example.budgetcontrol.db.BudgetControlDB
import com.example.budgetcontrol.db.model.Target
import com.example.budgetcontrol.dialog.ConfirmationDialog
import com.example.budgetcontrol.dialog.RecordDialog
import com.example.budgetcontrol.enum.FragmentType
import kotlinx.android.synthetic.main.target_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TargetFragment: Fragment() {

    //todo add target ids consts (two)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.target_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).setToolbarTitle(getString(R.string.target))

        fetchData()
        setConfirmButtonOnClickListener()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.toolbarMenuItemEdit).isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.toolbarMenuItemEdit) {
            handleEditButtonClick()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun handleEditButtonClick() {
        RecordDialog(
                requireContext(),
                context?.getString(R.string.target),
                context?.getString(R.string.target_description),
                this::updateTarget
        ).show()
    }

    private fun updateTarget(newAmount: Float, description: String) {
        val targetDao = BudgetControlDB.getInstance(requireContext()).targetDao()

        GlobalScope.launch {
            val currentTarget = targetDao.getTarget(Target.MAIN_TARGET_ID)
            if (newAmount < currentTarget.collectedAmount) {
                handleTargetAchievement()
            } else {
                val newTarget = currentTarget.copy(
                        amount = newAmount,
                        description = description
                )

                BudgetControlDB.getInstance(requireContext())
                        .targetDao()
                        .update(newTarget)

                activity?.runOnUiThread {
                    refreshTargetInfo()
                }
            }
        }
    }


    private fun fetchData() {
        GlobalScope.launch {
            val isTargetAdded = BudgetControlDB.getInstance(requireContext())
                    .targetDao()
                    .isAdded(Target.MAIN_TARGET_ID)

            activity?.runOnUiThread {
                if (isTargetAdded) {
                    refreshTargetInfo()
                } else {
                    createTarget()
                }
            }
        }
    }

    private fun createTarget() {
        val dialog = RecordDialog(
                requireContext(),
                context?.getString(R.string.target),
                context?.getString(R.string.target_description),
                this::recordTarget
        )
        dialog.setOnDismissListener {
            GlobalScope.launch {
                val isTargetAdded = BudgetControlDB.getInstance(requireContext())
                        .targetDao().isAdded(Target.MAIN_TARGET_ID)
                if (!isTargetAdded) {
                    activity?.runOnUiThread {
                        (activity as MainActivity).navigateToFragment(FragmentType.BUDGET)
                    }
                }
            }
        }
        dialog.show()
    }

    private fun refreshTargetInfo() {
        val targetDao = BudgetControlDB.getInstance(requireContext()).targetDao()
        var targetAmount: Float
        var targetCollectedAmount: Float
        var leftAmount: Float

        GlobalScope.launch {
            targetCollectedAmount = targetDao.getCollectedAmount(Target.MAIN_TARGET_ID)
            targetAmount = targetDao.getAmount(Target.MAIN_TARGET_ID)
            leftAmount = targetAmount - targetCollectedAmount

            activity?.runOnUiThread {
                targetCollectedAmountTextView.text = targetCollectedAmount.toString()
                targetAmountTextView.text = targetAmount.toString()
                leftAmountTextView.text = leftAmount.toString()
            }
        }
    }

    private fun setConfirmButtonOnClickListener() {
        confirmTargetContributionButton.setOnClickListener {
            if (!targetContributionEditText.text.isNullOrBlank()) {
                val targetDao = BudgetControlDB.getInstance(requireContext()).targetDao()

                GlobalScope.launch {
                    val currentCollectedAmount = targetDao.getCollectedAmount(Target.MAIN_TARGET_ID)
                    val contribution = targetContributionEditText.text.toString().toFloat()
                    targetContributionEditText.text.clear()
                    val newCollectedAmount = currentCollectedAmount + contribution
                    val targetAmount = targetDao.getAmount(Target.MAIN_TARGET_ID)

                    if (newCollectedAmount >= targetAmount) {
                        handleTargetAchievement()
                    } else {
                        targetDao.updateCollectedAmount(Target.MAIN_TARGET_ID, newCollectedAmount)

                        activity?.runOnUiThread {
                            refreshTargetInfo()
                        }
                    }
                }
            }
        }
    }

    private fun handleTargetAchievement() {
        val targetDao = BudgetControlDB.getInstance(requireContext()).targetDao()
        targetDao.delete(Target.MAIN_TARGET_ID)

        activity?.runOnUiThread {
            val dialog = ConfirmationDialog(
                    requireContext(),
                    getString(R.string.you_achieve_goal)
            )
            dialog.apply {
                confirmButton.setOnClickListener {
                    createTarget()
                    dismiss()
                }
                closeButton.setOnClickListener {
                    (activity as MainActivity).navigateToFragment(FragmentType.BUDGET)
                    dismiss()
                }
                show()
            }
            refreshTargetInfo()
        }
    }

    private fun recordTarget(amount: Float, description: String) {
        val target = Target(
                Target.MAIN_TARGET_ID,
                description,
                amount,
                0
        )

        GlobalScope.launch {
            BudgetControlDB.getInstance(requireContext())
                    .targetDao()
                    .insert(target)
            activity?.runOnUiThread {
                refreshTargetInfo()
            }
        }
    }
}