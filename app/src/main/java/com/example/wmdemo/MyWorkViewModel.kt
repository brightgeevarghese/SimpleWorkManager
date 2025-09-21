package com.example.wmdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MyWorkViewModel(
    private val workManager: WorkManager
): ViewModel() {
    private val _workUiState = MutableStateFlow(WorkUiState())
    val workUiState = _workUiState.asStateFlow()

    companion object {
        private const val UNIQUE_WORK_NAME = "my_work"
    }
    init {
        viewModelScope.launch {
            workManager.getWorkInfosForUniqueWorkFlow(UNIQUE_WORK_NAME)
                .collect { workInfos ->
                    if (workInfos.isNotEmpty()) {
                        val workInfo = workInfos.first()
                        _workUiState.value = WorkUiState(
                            state = workInfo.state,
                            outputData = workInfo.outputData.getString("output"),
                            error = workInfo.outputData.getString("error")
                        )
                    }
                }
        }
    }

    fun startWork() {
        val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresStorageNotLow(true)
//                    .setRequiresCharging(true)
                    .build()
            )
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        workManager.enqueueUniqueWork(
            UNIQUE_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }
    fun cancelWork() {
        workManager.cancelUniqueWork(UNIQUE_WORK_NAME)
    }
}