package com.example.wmdemo

import androidx.work.WorkInfo

data class WorkUiState(
    val state: WorkInfo.State? = null,
    val outputData: String? = null,
    val error: String? = null,
)
