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
        fetchData()

        setConfirmButtonOnClickListener()
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
//            (activity as MainActivity).navigateToFragment(FragmentType.BUDGET)
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
                        targetDao.deleteById(Target.MAIN_TARGET_ID)

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