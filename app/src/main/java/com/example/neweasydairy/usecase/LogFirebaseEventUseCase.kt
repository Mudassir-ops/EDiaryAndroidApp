package com.example.neweasydairy.usecase

import com.example.neweasydairy.domain.model.FirebaseEvent
import com.example.neweasydairy.domain.repository.FirebaseEventLogger
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogFirebaseEventUseCase @Inject constructor(
    private val eventLogger: FirebaseEventLogger
) {
    operator fun invoke(event: FirebaseEvent): Flow<Result<Unit>> {
        return eventLogger.logEvent(event)
    }
}