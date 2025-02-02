package com.rpsouza.planner.presenter.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.rpsouza.planner.R
import com.rpsouza.planner.data.utils.imageBase64ToBitmap
import com.rpsouza.planner.databinding.FragmentHomeBinding
import com.rpsouza.planner.presenter.bottom_sheet.UpdatePlannerActivityDialogFragment
import com.rpsouza.planner.presenter.signup.SignupViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val signupViewModel: SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserves()
        with(binding) {
            btnSaveNewPlannerActivity.setOnClickListener {
                UpdatePlannerActivityDialogFragment().show(
                    childFragmentManager,
                    UpdatePlannerActivityDialogFragment.TAG
                )
            }
        }
    }

    private fun setupObserves() {
        lifecycleScope.launch {
            signupViewModel.profile.collect { profile ->
                binding.tvUserName.text = getString(R.string.ola_usuario, profile.name)
                imageBase64ToBitmap(base46String = profile.image)?.let { imageBitmap ->
                    binding.ivUserPhoto.setImageBitmap(imageBitmap)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}