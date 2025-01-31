package com.rpsouza.planner.presenter.initial_loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rpsouza.planner.R
import com.rpsouza.planner.databinding.FragmentInitialLoadingBinding
import com.rpsouza.planner.presenter.signup.SignupViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InitialLoadingFragment : Fragment() {
    private var _binding: FragmentInitialLoadingBinding? = null
    private val binding get() = _binding!!

    private val navController by lazy { findNavController() }

    private val signupViewModel: SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInitialLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(2_000)
            val action = if (signupViewModel.getIsUserRegistered()) {
                R.id.action_initialLoadingFragment_to_homeFragment
            } else {
                R.id.action_initialLoadingFragment_to_signupFragment
            }
            navController.navigate(action)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}