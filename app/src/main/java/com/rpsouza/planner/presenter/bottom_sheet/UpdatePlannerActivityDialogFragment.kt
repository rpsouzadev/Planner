package com.rpsouza.planner.presenter.bottom_sheet

import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rpsouza.planner.databinding.FragmentUpadatePlannerActivityDialogBinding
import com.rpsouza.planner.domain.model.PlannerActivity
import com.rpsouza.planner.domain.utils.createCalendarFromTimeInMillis
import com.rpsouza.planner.domain.utils.toPlannerActivityDate
import com.rpsouza.planner.domain.utils.toPlannerActivityTime
import com.rpsouza.planner.presenter.component.PlannerActivityDataPickerDialogFragment
import com.rpsouza.planner.presenter.component.PlannerActivityTimePickerDialogFragment
import com.rpsouza.planner.presenter.extension.hideKeyboard
import com.rpsouza.planner.presenter.viewmodel.PlannerActivityViewModel
import com.rpsouza.planner.presenter.viewmodel.SetDate
import com.rpsouza.planner.presenter.viewmodel.SetTime


class UpdatePlannerActivityDialogFragment(
    private val selectActivity: PlannerActivity,
) : BottomSheetDialogFragment() {
    private var _binding: FragmentUpadatePlannerActivityDialogBinding? = null
    private val binding get() = _binding!!

    private val plannerActivityViewModel: PlannerActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpadatePlannerActivityDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        plannerActivityViewModel.setSelectActivity(selectActivity = selectActivity)

        with(binding) {
            val calendarOfSelectActivity =
                createCalendarFromTimeInMillis(timeInMillis = selectActivity.datetime)

            tietName.setText(selectActivity.name)
            tietDate.setText(calendarOfSelectActivity.toPlannerActivityDate())
            tietTime.setText(calendarOfSelectActivity.toPlannerActivityTime())

            tietName.doOnTextChanged { text, _, _, _ ->
                if (text.isNullOrEmpty()) {
                    tietName.clearFocus()
                    requireContext().hideKeyboard(fromView = tietName)
                }

                plannerActivityViewModel.updateSelectActivity(name = text.toString())
            }

            tietDate.setOnClickListener {
                PlannerActivityDataPickerDialogFragment(
                    initialDate = createCalendarFromTimeInMillis(selectActivity.datetime),
                    onConfirm = { year, month, day ->
                        val filledCalendar = Calendar.getInstance().apply {
                            set(Calendar.YEAR, year)
                            set(Calendar.MONTH, month)
                            set(Calendar.DAY_OF_MONTH, day)
                        }

                        tietDate.setText(filledCalendar.toPlannerActivityDate())
                        plannerActivityViewModel.updateSelectActivity(
                            date = SetDate(
                                year = year,
                                month = month,
                                dayOfMonth = day
                            )
                        )
                    },
                    onCancel = {}
                ).show(
                    childFragmentManager,
                    PlannerActivityDataPickerDialogFragment.TAG
                )
            }

            tietTime.setOnClickListener {
                PlannerActivityTimePickerDialogFragment(
                    initialTime = createCalendarFromTimeInMillis(selectActivity.datetime),
                    onConfirm = { hour, minute ->
                        val filledCalendar = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, hour)
                            set(Calendar.MINUTE, minute)
                        }

                        tietTime.setText(filledCalendar.toPlannerActivityTime())
                        plannerActivityViewModel.updateSelectActivity(
                            time = SetTime(
                                hour = hour,
                                minute = minute
                            )
                        )
                    },
                    onCancel = {}
                ).show(
                    childFragmentManager,
                    PlannerActivityTimePickerDialogFragment.TAG
                )
            }

            btnDelete.setOnClickListener {
                plannerActivityViewModel.deleteByUuid(uuid = selectActivity.uuid)
                dialog?.dismiss()
            }

            btnUpdatePlannerActivity.setOnClickListener {
                plannerActivityViewModel.saveUpdatedSelectActivity()
                dialog?.dismiss()
            }

        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        plannerActivityViewModel.clearSelectActivity()
    }

    companion object {
        const val TAG = "UpdatePlannerActivityDialogFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}