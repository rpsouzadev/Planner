package com.rpsouza.planner

import android.app.Application
import com.rpsouza.planner.data.di.MainServiceLocator

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MainServiceLocator.initialize(application = this)
    }

}