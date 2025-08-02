package com.simpol.permissionssummary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppRepository(application)

    private val _permissionGroups = MutableStateFlow<List<PermissionGroup>>(emptyList())
    val permissionGroups: StateFlow<List<PermissionGroup>> = _permissionGroups.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    private val _allPermissions = MutableStateFlow<List<String>>(emptyList())
    val allPermissions: StateFlow<List<String>> = _allPermissions.asStateFlow()

    private val _allApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val allApps: StateFlow<List<AppInfo>> = _allApps.asStateFlow()

    fun loadPermissionGroups() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _permissionGroups.value = repository.getPermissionGroups(_filterState.value)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadAllPermissionsAndApps() {
        viewModelScope.launch {
            try {
                _allPermissions.value = repository.getAllPermissions()
                _allApps.value = repository.getAllApps()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun hidePermission(permission: String) {
        _filterState.value = _filterState.value.copy(
            hiddenPermissions = _filterState.value.hiddenPermissions + permission
        )
        loadPermissionGroups()
    }

    fun showPermission(permission: String) {
        _filterState.value = _filterState.value.copy(
            hiddenPermissions = _filterState.value.hiddenPermissions - permission
        )
        loadPermissionGroups()
    }

    fun hideApp(packageName: String) {
        _filterState.value = _filterState.value.copy(
            hiddenApps = _filterState.value.hiddenApps + packageName
        )
        loadPermissionGroups()
    }

    fun showApp(packageName: String) {
        _filterState.value = _filterState.value.copy(
            hiddenApps = _filterState.value.hiddenApps - packageName
        )
        loadPermissionGroups()
    }

    fun clearAllFilters() {
        _filterState.value = FilterState()
        loadPermissionGroups()
    }
}
