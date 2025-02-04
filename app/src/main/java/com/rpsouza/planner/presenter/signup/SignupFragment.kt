package com.rpsouza.planner.presenter.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rpsouza.planner.R
import com.rpsouza.planner.domain.utils.imageBitmapToBase64
import com.rpsouza.planner.domain.utils.imageUriToBitmap
import com.rpsouza.planner.databinding.FragmentSignupBinding
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val navController by lazy { findNavController() }
    private val signupViewModel: SignupViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        with(binding) {
            ivAddPhoto.setOnClickListener {
                pickMedia.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }

            tietName.addTextChangedListener { text ->
                signupViewModel.updateProfile(name = text.toString())
            }

            tietEmail.addTextChangedListener { text ->
                signupViewModel.updateProfile(email = text.toString())
            }

            tietPhone.addTextChangedListener { text ->
                signupViewModel.updateProfile(phone = text.toString())
            }

            btnSaveUser.setOnClickListener {
                signupViewModel.saveProfile(
                    onCompleted = {
                        navController.navigate(R.id.action_signupFragment_to_homeFragment)
                    }
                )
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            signupViewModel.isProfileValid.collect { isValid ->
                binding.btnSaveUser.isEnabled = isValid
            }
        }
    }

    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { uriImage ->
            val imageBitmap = requireContext().imageUriToBitmap(uriImage)
            imageBitmap?.let { bitmap ->
                val base64Image = imageBitmapToBase64(bitmap)
                signupViewModel.updateProfile(image = base64Image)
                binding.ivAddPhoto.setImageURI(uriImage)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}