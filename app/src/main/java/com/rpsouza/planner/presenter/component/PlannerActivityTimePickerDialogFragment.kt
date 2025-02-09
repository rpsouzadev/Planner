package com.rpsouza.planner.presenter.component

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.rpsouza.planner.R

class PlannerActivityTimePickerDialogFragment(
    private val initialTime: Calendar? = null,
    private val onConfirm: (Int, Int) -> Unit,
    private val onCancel: () -> Unit
) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = initialTime ?: Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val customTimePickerDialog = TimePickerDialog(
            requireContext(),
            this,
            hour,
            minute,
            DateFormat.is24HourFormat(requireContext())
        ).setupPlannerTimePicker()

        return customTimePickerDialog
    }

    private fun TimePickerDialog.setupPlannerTimePicker(): TimePickerDialog {
        return this.apply {

            window?.decorView?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.lime_950
                )
            )

            setButton(
                DialogInterface.BUTTON_POSITIVE,
                getString(R.string.confirm)
            ) { _, _ -> }

            setButton(
                DialogInterface.BUTTON_NEGATIVE,
                getString(R.string.cancel)
            ) { _, _ ->
                onCancel()
            }
        }
    }


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        onConfirm(hourOfDay, minute)
    }

    companion object {
        const val TAG = "PlannerActivityTimePickerDialogFragment"
    }
}
