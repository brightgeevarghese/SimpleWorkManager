package com.example.wmdemo

import android.R.attr.onClick
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.wmdemo.ui.theme.WmdemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WmdemoTheme {
                WorkScreen()
            }
        }
    }
}

@Composable
fun WorkScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val context = LocalContext.current
            val workViewModel: MyWorkViewModel = viewModel {
                MyWorkViewModel(WorkManager.getInstance(context = context))
            }
            val workUiState by workViewModel.workUiState.collectAsStateWithLifecycle()
            Button(
                onClick = {
                    workViewModel.startWork()
                }
            ) {
                Text(text = "Start Work")
            }
            Button(
                onClick = {
                    workViewModel.cancelWork()
                }
            ) {
                Text(text = "Cancel Work")
            }
            when(workUiState.state) {
                WorkInfo.State.ENQUEUED -> Text(text = "Work Enqueued")
                WorkInfo.State.RUNNING -> Text(text = "Work Running")
                WorkInfo.State.SUCCEEDED -> Text(text = "Work Succeeded")
                WorkInfo.State.FAILED -> Text(text = "Work Failed")
                WorkInfo.State.BLOCKED -> Text(text = "Work Blocked")
                WorkInfo.State.CANCELLED -> Text(text = "Work Cancelled")
                null -> Text(text = "Work Not Started")
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun WorkScreenPreview() {
    WmdemoTheme {
        WorkScreen()
    }
}
