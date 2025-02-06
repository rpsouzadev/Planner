package com.rpsouza.planner.data.datasource

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.rpsouza.planner.domain.model.Profile
import com.rpsouza.planner.domain.model.ProfileSerializer
import kotlinx.coroutines.flow.Flow

private const val PROFILE_FILE_NAME = "profile.db"
private const val USER_REGISTRATION_FILE_NAME = "USER_REGISTRATION_FILE_NAME"
private const val IS_USER_REGISTERED = "IS_USER_REGISTERED"

class UserRegistrationLocalDataSourceImpl(
    private val applicationContext: Context
) : UserRegistrationLocalDataSource {

    private val Context.profileProtoDataStore: DataStore<Profile> by dataStore(
        fileName = PROFILE_FILE_NAME,
        serializer = ProfileSerializer
    )

    private val userRegistrationSharedPreferences: SharedPreferences =
        applicationContext.getSharedPreferences(USER_REGISTRATION_FILE_NAME, Context.MODE_PRIVATE)

    override fun getIsUserRegistered(): Boolean {
        return userRegistrationSharedPreferences.getBoolean(IS_USER_REGISTERED, false)
    }

    override fun saveIsUserRegistered(isUserRegistered: Boolean) {
        with(userRegistrationSharedPreferences.edit()) {
            putBoolean(IS_USER_REGISTERED, isUserRegistered)
            apply()
        }
    }

    override val profile: Flow<Profile>
        get() = applicationContext.profileProtoDataStore.data

    override suspend fun saveProfile(profile: Profile) {
        applicationContext.profileProtoDataStore.updateData {
            profile
        }
    }

}
