package com.example.neweasydairy.domain.repository

import com.example.neweasydairy.domain.model.FirebaseEvent
import kotlinx.coroutines.flow.Flow

interface FirebaseEventLogger {
    fun logEvent(event: FirebaseEvent): Flow<Result<Unit>>

}