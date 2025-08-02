package com.simpol.permissionssummary

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.view.WindowCompat
import com.simpol.permissionssummary.ui.theme.PermissionsSummaryTheme
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        val view = findViewById<View>(android.R.id.content)
        val windowInsetsController = WindowInsetsControllerCompat(window, view)

        windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
        WindowCompat.setDecorFitsSystemWindows(window, true)

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
