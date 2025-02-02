package com.rpsouza.planner.data.datasource

import com.rpsouza.planner.data.model.Profile
import kotlinx.coroutines.flow.Flow

interface UserRegistrationLocalDataSource {

    fun getIsUserRegistered(): Boolean
    fun saveIsUserRegistered(isUserRegistered: Boolean)

    val profile: Flow<Profile>
    suspend fun saveProfile(profile: Profile)
}