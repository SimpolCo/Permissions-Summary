package com.simpol.permissionssummary

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.simpol.permissionssummary.ui.theme.PermissionsSummaryTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // FIRST
        setContent {
            PermissionsSummaryTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppListScreen()
                }
            }
        }
    }
}
