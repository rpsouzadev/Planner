package com.rpsouza.planner.presenter.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rpsouza.planner.databinding.FragmentUpadatePlannerActivityDialogBinding
import com.rpsouza.planner.domain.model.PlannerActivity
import com.rpsouza.planner.domain.utils.createCalendarFromTimeInMillis
import com.rpsouza.planner.domain.utils.toPlannerActivityDate
import com.rpsouza.planner.domain.utils.toPlannerActivityTime


class UpdatePlannerActivityDialogFragment(
    private val selectActivity: PlannerActivity,
) : BottomSheetDialogFragment() {
    private var _binding: FragmentUpadatePlannerActivityDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpadatePlannerActivityDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val calendarOfSelectActivity =
                createCalendarFromTimeInMillis(timeInMillis = selectActivity.datetime)

            tietName.setText(selectActivity.name)
            tietDate.setText(calendarOfSelectActivity.toPlannerActivityDate())
            tietTime.setText(calendarOfSelectActivity.toPlannerActivityTime())
        }
    }

    companion object {
        const val TAG = "UpdatePlannerActivityDialogFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}