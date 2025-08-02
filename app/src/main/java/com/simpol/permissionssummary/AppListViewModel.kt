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

    private val _permissionGroups = MutableStateFlow<List<PermissionGroup>>(emptyList())
    val permissionGroups: StateFlow<List<PermissionGroup>> = _permissionGroups.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadPermissionGroups() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _permissionGroups.value = repository.getPermissionGroups()
            } catch (e: Exception) {
                // Handle error - you might want to add error state
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}