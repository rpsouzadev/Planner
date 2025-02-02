package com.rpsouza.planner.data.datasource.auth

import kotlinx.coroutines.flow.Flow

interface AuthenticationLocalDataSource {
    val token: Flow<String>
    val expirationDateTime: Flow<Long>

    suspend fun insertToken(token: String)
}