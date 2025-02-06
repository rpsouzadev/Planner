package com.rpsouza.planner.presenter.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.rpsouza.planner.R
import com.rpsouza.planner.databinding.FragmentHomeBinding
import com.rpsouza.planner.domain.utils.imageBase64ToBitmap
import com.rpsouza.planner.presenter.bottom_sheet.UpdatePlannerActivityDialogFragment
import com.rpsouza.planner.presenter.component.PlannerActivityDataPickerDialogFragment
import com.rpsouza.planner.presenter.component.PlannerActivityTimePickerDialogFragment
import com.rpsouza.planner.presenter.viewmodel.SignupViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val signupViewModel: SignupViewModel by activityViewModels()

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
            tietNewPlannerActivityDate.setOnClickListener {
                PlannerActivityDataPickerDialogFragment(
                    onConfirm = { year, month, day ->
                        Toast.makeText(requireContext(), "$year $month, $day", Toast.LENGTH_SHORT)
                            .show()
                    },
                    onCancel = {}
                ).show(
                    childFragmentManager,
                    PlannerActivityDataPickerDialogFragment.TAG
                )
            }

            tietNewPlannerActivityTime.setOnClickListener {
                PlannerActivityTimePickerDialogFragment(
                    onConfirm = { hour, minute ->
                        Toast.makeText(requireContext(), "$hour $minute", Toast.LENGTH_SHORT).show()
                    },
                    onCancel = {}
                ).show(
                    childFragmentManager,
                    PlannerActivityTimePickerDialogFragment.TAG
                )
            }

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
            launch {
                signupViewModel.profile.collect { profile ->
                    binding.tvUserName.text = getString(R.string.ola_usuario, profile.name)
                    imageBase64ToBitmap(base46String = profile.image)?.let { imageBitmap ->
                        binding.ivUserPhoto.setImageBitmap(imageBitmap)
                    }
                }
            }

            launch {
                signupViewModel.isTokenValid.distinctUntilChanged { old, new ->
                    old == new
                }.collect { isTokenValid ->
                    if (!isTokenValid) {
                        showNewTokenSnackBar()
                    }
                }
            }
        }
    }

    private fun showNewTokenSnackBar() {
        Snackbar.make(
            requireView(),
            getString(R.string.seu_token_expirou),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(getString(R.string.obter_novo_token)) {
            signupViewModel.obtainNewToken()
        }.setActionTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.lime_300
            )
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}