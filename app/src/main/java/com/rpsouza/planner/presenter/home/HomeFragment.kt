package com.rpsouza.planner.presenter.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rpsouza.planner.databinding.FragmentHomeBinding
import com.rpsouza.planner.presenter.bottom_sheet.UpdatePlannerActivityDialogFragment

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnSaveNewPlannerActivity.setOnClickListener {
                UpdatePlannerActivityDialogFragment().show(
                    childFragmentManager,
                    UpdatePlannerActivityDialogFragment.TAG
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}