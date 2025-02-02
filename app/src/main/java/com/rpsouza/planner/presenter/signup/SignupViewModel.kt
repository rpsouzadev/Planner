package com.rpsouza.planner.presenter.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rpsouza.planner.data.datasource.UserRegistrationLocalDataSource
import com.rpsouza.planner.data.di.MainServiceLocator
import com.rpsouza.planner.data.model.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {
    private val _profile: MutableStateFlow<Profile> = MutableStateFlow(Profile())
    val profile: StateFlow<Profile> = _profile.asStateFlow()

    private val _isProfileValid: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isProfileValid: StateFlow<Boolean> = _isProfileValid.asStateFlow()

    private val userRegistrationLocalDataSource: UserRegistrationLocalDataSource by lazy {
        MainServiceLocator.userRegistrationLocalDataSource
    }

    init {
        viewModelScope.launch {
            userRegistrationLocalDataSource.profile.collect { currentProfile ->
                _profile.update { currentProfile }
            }
        }
    }

    fun getIsUserRegistered(): Boolean {
        return userRegistrationLocalDataSource.getIsUserRegistered()
    }

    fun updateProfile(
        name: String? = null,
        email: String? = null,
        phone: String? = null,
        image: String? = null
    ) {
        if (name == null && email == null && phone == null && image == null) return

        _profile.update { currentProfile ->
            val updatedProfile = currentProfile.copy(
                name = name ?: currentProfile.name,
                email = email ?: currentProfile.email,
                phone = phone ?: currentProfile.phone,
                image = image ?: currentProfile.image
            )
            _isProfileValid.update { updatedProfile.isValid() }
            updatedProfile
        }
    }

    fun saveProfile() {
        viewModelScope.launch {
            userRegistrationLocalDataSource.saveProfile(profile = _profile.value)
            userRegistrationLocalDataSource.saveIsUserRegistered(isUserRegistered = true)
        }

    }
}