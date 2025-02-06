package com.rpsouza.planner.core.di

import android.app.Application
import androidx.room.Room
import com.rpsouza.planner.data.database.DatabaseConstants.DATABASE_NAME
import com.rpsouza.planner.data.database.dao.PlannerActivityDao
import com.rpsouza.planner.data.database.db.PlannerActivityDatabase
import com.rpsouza.planner.data.datasource.AuthenticationLocalDataSource
import com.rpsouza.planner.data.datasource.UserRegistrationLocalDataSource
import com.rpsouza.planner.data.datasource.UserRegistrationLocalDataSourceImpl
import com.rpsouza.planner.data.datasource.AuthenticationLocalDataSourceImpl
import com.rpsouza.planner.data.datasource.PlannerActivityLocalDataSource
import com.rpsouza.planner.data.datasource.PlannerActivityLocalDataSourceImpl

object MainServiceLocator {
    private var _application: Application? = null
    private val application: Application get() = _application!!

    val userRegistrationLocalDataSource: UserRegistrationLocalDataSource by lazy {
        UserRegistrationLocalDataSourceImpl(applicationContext = application.applicationContext)
    }

    val authenticationLocalDataSource: AuthenticationLocalDataSource by lazy {
        AuthenticationLocalDataSourceImpl(applicationContext = application.applicationContext)
    }

    private val plannerActivityDao: PlannerActivityDao by lazy {
        val database = Room.databaseBuilder(
            application.applicationContext,
            PlannerActivityDatabase::class.java,
            DATABASE_NAME
        ).build()

        database.plannerActivityDao()
    }

    val plannerActivityLocalDataSource: PlannerActivityLocalDataSource by lazy {
        PlannerActivityLocalDataSourceImpl(plannerActivityDao)
    }


    fun initialize(application: Application) {
        _application = application
    }

    fun clear() {
        _application = null
    }
}