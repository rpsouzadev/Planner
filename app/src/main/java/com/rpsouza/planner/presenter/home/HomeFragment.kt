package com.rpsouza.planner.presenter.home

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.rpsouza.planner.R
import com.rpsouza.planner.databinding.FragmentHomeBinding
import com.rpsouza.planner.domain.utils.imageBase64ToBitmap
import com.rpsouza.planner.domain.utils.toPlannerActivityDate
import com.rpsouza.planner.domain.utils.toPlannerActivityTime
import com.rpsouza.planner.presenter.bottom_sheet.UpdatePlannerActivityDialogFragment
import com.rpsouza.planner.presenter.component.PlannerActivityAdapter
import com.rpsouza.planner.presenter.component.PlannerActivityDataPickerDialogFragment
import com.rpsouza.planner.presenter.component.PlannerActivityTimePickerDialogFragment
import com.rpsouza.planner.presenter.extension.hideKeyboard
import com.rpsouza.planner.presenter.viewmodel.PlannerActivityViewModel
import com.rpsouza.planner.presenter.viewmodel.SetDate
import com.rpsouza.planner.presenter.viewmodel.SetTime
import com.rpsouza.planner.presenter.viewmodel.SignupViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val signupViewModel: SignupViewModel by activityViewModels()
    private val plannerActivityViewModel: PlannerActivityViewModel by activityViewModels()

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
            plannerActivityViewModel.fetchActivities()

            clContainer.setOnClickListener {
                tietNewPlannerActivityName.clearFocus()
                requireContext().hideKeyboard(fromView = tietNewPlannerActivityName)
            }

            tietNewPlannerActivityName.doOnTextChanged { text, _, _, _ ->
                if (text.isNullOrEmpty()) {
                    tietNewPlannerActivityName.clearFocus()
                    requireContext().hideKeyboard(fromView = tietNewPlannerActivityName)
                }

                plannerActivityViewModel.updateNewActivity(name = text.toString())
            }

            tietNewPlannerActivityDate.setOnClickListener {
                PlannerActivityDataPickerDialogFragment(
                    onConfirm = { year, month, day ->
                        val filledCalendar = Calendar.getInstance().apply {
                            set(Calendar.YEAR, year)
                            set(Calendar.MONTH, month)
                            set(Calendar.DAY_OF_MONTH, day)
                        }

                        tietNewPlannerActivityDate.setText(filledCalendar.toPlannerActivityDate())
                        plannerActivityViewModel.updateNewActivity(
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

            tietNewPlannerActivityTime.setOnClickListener {
                PlannerActivityTimePickerDialogFragment(
                    onConfirm = { hour, minute ->
                        val filledCalendar = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, hour)
                            set(Calendar.MINUTE, minute)
                        }

                        tietNewPlannerActivityTime.setText(filledCalendar.toPlannerActivityTime())
                        plannerActivityViewModel.updateNewActivity(
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

            btnSaveNewPlannerActivity.setOnClickListener {
                plannerActivityViewModel.saveNewActivity(
                    onSuccess = {
                        clearNewPlannerActivityFields()
                    },
                    onError = {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.oops_houve_uma_falha_ao_salvar_a_atividade),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }
        }
    }

    private fun clearNewPlannerActivityFields() {
        with(binding) {
            tietNewPlannerActivityName.text?.clear()
            tietNewPlannerActivityDate.text?.clear()
            tietNewPlannerActivityTime.text?.clear()
            requireContext().hideKeyboard(fromView = tietNewPlannerActivityName)
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

            launch {
                plannerActivityViewModel.activities.collect { activities ->
                    with(binding) {
                        if (rvPlannerActivity.adapter == null)
                            rvPlannerActivity.adapter = PlannerActivityAdapter(
                                onClickPlannerActivity = { selectActivity ->
                                    UpdatePlannerActivityDialogFragment(
                                        selectActivity = selectActivity,
                                    ).show(
                                        childFragmentManager,
                                        UpdatePlannerActivityDialogFragment.TAG
                                    )
                                },
                                onChangeIsCompleted = { updateIsCompleted, selectActivity ->
                                    plannerActivityViewModel.updateIsCompleted(
                                        uuid = selectActivity.uuid,
                                        isCompleted = updateIsCompleted
                                    )
                                }
                            )

                        (rvPlannerActivity.adapter as PlannerActivityAdapter).submitList(
                            activities
                        )
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