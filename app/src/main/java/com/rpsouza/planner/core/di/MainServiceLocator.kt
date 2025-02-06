package com.rpsouza.planner.core.di

import android.app.Application
import androidx.room.Room
import com.rpsouza.planner.data.database.DatabaseConstants.DATABASE_NAME
import com.rpsouza.planner.data.database.dao.PlannerActivityDao
import com.rpsouza.planner.data.database.db.PlannerActivityDatabase
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

    val plannerActivityDao: PlannerActivityDao by lazy {
        val database = Room.databaseBuilder(
            application.applicationContext,
            PlannerActivityDatabase::class.java,
            DATABASE_NAME
        ).build()

        database.plannerActivityDao()
    }


    fun initialize(application: Application) {
        _application = application
    }

    fun clear() {
        _application = null
    }
}