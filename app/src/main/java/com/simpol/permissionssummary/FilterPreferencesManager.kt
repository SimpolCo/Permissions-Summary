package com.simpol.permissionssummary

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "filter_prefs")

class FilterPreferencesManager(private val context: Context) {
    private val HIDDEN_PERMISSIONS = stringSetPreferencesKey("hidden_permissions")
    private val HIDDEN_APPS = stringSetPreferencesKey("hidden_apps")

    val filterStateFlow: Flow<FilterState> = context.dataStore.data.map { prefs ->
        val hiddenPermissions = prefs[HIDDEN_PERMISSIONS] ?: emptySet()
        val hiddenApps = prefs[HIDDEN_APPS] ?: emptySet()
        FilterState(hiddenPermissions, hiddenApps)
    }

    suspend fun saveFilterState(state: FilterState) {
        context.dataStore.edit { prefs ->
            prefs[HIDDEN_PERMISSIONS] = state.hiddenPermissions
            prefs[HIDDEN_APPS] = state.hiddenApps
        }
    }

    suspend fun clearFilters() {
        context.dataStore.edit { prefs ->
            prefs[HIDDEN_PERMISSIONS] = emptySet()
            prefs[HIDDEN_APPS] = emptySet()
        }
    }
}
