package com.rpsouza.planner.data.di

import android.app.Application
import com.rpsouza.planner.data.datasource.UserRegistrationLocalDataSource
import com.rpsouza.planner.data.datasource.UserRegistrationLocalDataSourceImpl
import com.rpsouza.planner.data.datasource.auth.AuthenticationLocalDataSource
import com.rpsouza.planner.data.datasource.auth.AuthenticationLocalDataSourceImpl

object MainServiceLocator {
    private var _application: Application? = null
    private val application: Application get() = _application!!

   val userRegistrationLocalDataSource: UserRegistrationLocalDataSource by lazy {
        UserRegistrationLocalDataSourceImpl(applicationContext = application.applicationContext)
   }

    val authenticationLocalDataSource: AuthenticationLocalDataSource by lazy {
        AuthenticationLocalDataSourceImpl(applicationContext = application.applicationContext)
    }


    fun initialize(application: Application){
        _application = application
    }

    fun clear() {
        _application = null
    }
}