package com.example.neweasydairy.data.remote

import com.example.neweasydairy.data.mapper.toData
import com.example.neweasydairy.domain.model.FirebaseEvent
import com.example.neweasydairy.domain.repository.FirebaseEventLogger
import com.example.neweasydairy.source.FirebaseAnalyticsDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FirebaseEventLoggerImpl @Inject constructor(
    private val dataSource: FirebaseAnalyticsDataSource
) : FirebaseEventLogger {

    override fun logEvent(event: FirebaseEvent): Flow<Result<Unit>> = flow {
        try {
            val eventData = event.toData()
            dataSource.logEvent(eventData.name ?: "", eventData.params ?: emptyMap())
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

}