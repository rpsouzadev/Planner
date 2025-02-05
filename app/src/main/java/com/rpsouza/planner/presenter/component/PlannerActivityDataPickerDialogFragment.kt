package com.rpsouza.planner.presenter.component

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.rpsouza.planner.R

class PlannerActivityDataPickerDialogFragment(
    private val onConfirm: (Int, Int, Int) -> Unit,
    private val onCancel: () -> Unit
) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val customDatePickerDialog = DatePickerDialog(
            requireContext(),
            this,
            year,
            month,
            day
        ).setupPlannerDatePicker(minDate = calendar.timeInMillis)

        return customDatePickerDialog
    }

    private fun DatePickerDialog.setupPlannerDatePicker(minDate: Long): DatePickerDialog {
        return this.apply {
            datePicker.minDate = minDate

            window?.decorView?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.lime_950
                )
            )

            datePicker.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )

            setButton(
                DialogInterface.BUTTON_POSITIVE,
                getString(R.string.confirm)
            ) { _, _ ->
                onConfirm(datePicker.year, datePicker.month, datePicker.dayOfMonth)
            }

            setButton(
                DialogInterface.BUTTON_NEGATIVE,
                getString(R.string.cancel)
            ) { _, _ ->
                onCancel()
            }
        }
    }

    override fun onDateSet(
        view: DatePicker?,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int
    ) {
        // Obs: só seria utilizado caso não houvesse a sobrescrita do botão primário de confirmação.
    }

    companion object {
        const val TAG = "PlannerActivityDataPickerDialogFragment"
    }

}