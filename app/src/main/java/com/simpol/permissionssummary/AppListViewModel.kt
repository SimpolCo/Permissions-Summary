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

    private val prefs = FilterPreferencesManager(application)

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


    init {
        viewModelScope.launch {
            prefs.filterStateFlow.collect { savedState ->
                _filterState.value = savedState
                loadPermissionGroups()
            }
        }
    }

    private fun updateAndPersistFilterState(newState: FilterState) {
        _filterState.value = newState
        viewModelScope.launch {
            prefs.saveFilterState(newState)
        }
        loadPermissionGroups()
    }

    fun hidePermission(permission: String) {
        updateAndPersistFilterState(
            _filterState.value.copy(
                hiddenPermissions = _filterState.value.hiddenPermissions + permission
            )
        )
    }

    fun showPermission(permission: String) {
        updateAndPersistFilterState(
            _filterState.value.copy(
                hiddenPermissions = _filterState.value.hiddenPermissions - permission
            )
        )
    }

    fun hideApp(packageName: String) {
        updateAndPersistFilterState(
            _filterState.value.copy(
                hiddenApps = _filterState.value.hiddenApps + packageName
            )
        )
    }

    fun showApp(packageName: String) {
        updateAndPersistFilterState(
            _filterState.value.copy(
                hiddenApps = _filterState.value.hiddenApps - packageName
            )
        )
    }

    fun clearAllFilters() {
        val cleared = FilterState()
        updateAndPersistFilterState(cleared)
        viewModelScope.launch {
            prefs.clearFilters()
        }
    }
}
