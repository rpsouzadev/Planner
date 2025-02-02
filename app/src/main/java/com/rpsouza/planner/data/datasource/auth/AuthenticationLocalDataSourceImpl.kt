package com.rpsouza.planner.data.datasource.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val AUTHENTICATION_FILE_NAME = "authentication"
private const val TOKEN_KEY = "token"
private val TOKEN_PREFERENCES_KEY = stringPreferencesKey(TOKEN_KEY)
private const val EXPIRATION_DATETIME_KEY = "expiration_datetime"
private val EXPIRATION_DATETIME_PREFERENCES_KEY = longPreferencesKey(EXPIRATION_DATETIME_KEY)
private const val ADDITIONAL_EXPIRATION_DATETIME_MILLIS = 10_000

class AuthenticationLocalDataSourceImpl(
    private val applicationContext: Context
) : AuthenticationLocalDataSource {

    private val Context.authenticationPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
        name = AUTHENTICATION_FILE_NAME
    )

    override val token: Flow<String>
        get() = applicationContext.authenticationPreferencesDataStore.data
            .map { preferences ->
                preferences[TOKEN_PREFERENCES_KEY] ?: ""
            }

    override val expirationDateTime: Flow<Long>
        get() = applicationContext.authenticationPreferencesDataStore.data
            .map { preferences ->
                preferences[EXPIRATION_DATETIME_PREFERENCES_KEY] ?: 0L
            }

    override suspend fun insertToken(token: String) {
        applicationContext.authenticationPreferencesDataStore.edit { preferences ->
            preferences[TOKEN_PREFERENCES_KEY] = token
            preferences[EXPIRATION_DATETIME_PREFERENCES_KEY] =
                System.currentTimeMillis() + ADDITIONAL_EXPIRATION_DATETIME_MILLIS
        }
    }
}