package com.example.neweasydairy.utilis

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.neweasydairy.domain.model.FirebaseEvent
import com.example.neweasydairy.usecase.LogFirebaseEventUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

object AppEventLogger {
    private var loggerUseCase: LogFirebaseEventUseCase? = null
    fun init(useCase: LogFirebaseEventUseCase) {
        loggerUseCase = useCase
    }

    fun CoroutineScope.logEventWithScope(
        name: String, params: Map<String, Any> = emptyMap()
    ) {
        loggerUseCase?.invoke(
            FirebaseEvent(name, params)
        )?.onEach {
            it.onSuccess {
                Log.d("AppEventLogger", "Logged: $name")
            }.onFailure { ex ->
                Log.e("AppEventLogger", "Failed to log event: $name", ex)
            }
        }?.launchIn(this)
    }
}
