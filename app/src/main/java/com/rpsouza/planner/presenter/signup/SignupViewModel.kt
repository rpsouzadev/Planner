package com.rpsouza.planner.presenter.signup

import androidx.lifecycle.ViewModel
import com.rpsouza.planner.data.datasource.UserRegistrationLocalDataSource
import com.rpsouza.planner.data.di.MainServiceLocator

class SignupViewModel: ViewModel() {

    private val userRegistrationLocalDataSource: UserRegistrationLocalDataSource by lazy {
        MainServiceLocator.userRegistrationLocalDataSource
    }

    fun saveIsUserRegistered(isUserRegistered: Boolean) {
        userRegistrationLocalDataSource.saveIsUserRegistered(isUserRegistered)
    }

    fun getIsUserRegistered(): Boolean {
        return userRegistrationLocalDataSource.getIsUserRegistered()
    }

}