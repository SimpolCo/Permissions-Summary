package com.simpol.permissionssummary

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simpol.permissionssummary.ui.theme.PermissionsSummaryTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PermissionsSummaryTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: AppListViewModel = viewModel()
                    AppListScreen(viewModel = viewModel)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onResume() {
        super.onResume()
        val vm = ViewModelProvider(this)[AppListViewModel::class.java]
        vm.loadPermissionGroups()
    }

}

