package com.example.neweasydairy.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neweasydairy.data.UpdateState
import com.example.neweasydairy.utilis.UpdateManagerWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private var updateManagerWrapper: UpdateManagerWrapper? = null

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val updateState: StateFlow<UpdateState> = _updateState

    fun init(activity: AppCompatActivity) {
        if (updateManagerWrapper == null) {
            updateManagerWrapper = UpdateManagerWrapper(context, activity).also {
                observe(it)
            }
        }
    }

    private fun observe(wrapper: UpdateManagerWrapper) {
        viewModelScope.launch {
            wrapper.installStatus.collect { state -> _updateState.value = state }
        }
    }

    fun checkForUpdates() = updateManagerWrapper?.checkForUpdates()
    fun completeUpdate() = updateManagerWrapper?.completeUpdate()
    fun unregisterListener() = updateManagerWrapper?.unregisterListener()
    fun checkDownloadedOnResume() = updateManagerWrapper?.checkForDownloadedUpdateOnResume()
}