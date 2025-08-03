package com.simpol.permissionssummary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppListViewModel(application: Application) : AndroidViewModel(application) {

    // --- Dependencies ---
    private val repository = AppRepository(application)

    // --- UI State ---
    private val _permissionsGroups = MutableStateFlow<List<PermissionsGroup>>(emptyList())
    val permissionsGroups: StateFlow<List<PermissionsGroup>> = _permissionsGroups.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Observe persisted filters and reload data accordingly
        viewModelScope.launch {
            loadPermissionGroups()
        }
    }

    /** Loads all unique permissions and installed apps */
    fun loadPermissionGroups() {
        viewModelScope.launch {
            try {
                _permissionsGroups.value = repository.getPermissionGroups()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
