package com.simpol.permissionssummary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppRepository(application)

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps: StateFlow<List<AppInfo>> = _apps.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadApps() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _apps.value = repository.getInstalledApps()
            } catch (e: Exception) {
                // Handle error - you might want to add error state
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}