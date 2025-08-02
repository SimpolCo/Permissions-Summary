package com.simpol.permissionssummary

import android.content.Context
import android.content.pm.PackageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {

    suspend fun getPermissionGroups(): List<PermissionGroup> = withContext(Dispatchers.IO) {
        val apps = getInstalledApps()
        val permissionToAppsMap = mutableMapOf<String, MutableList<AppInfo>>()

        // Group apps by permissions
        apps.forEach { app ->
            app.permissions.forEach { permission ->
                permissionToAppsMap.getOrPut(permission) { mutableListOf() }.add(app)
            }
        }

        // Convert to PermissionGroup list and sort by number of apps (most used permissions first)
        permissionToAppsMap.map { (permission, appList) ->
            PermissionGroup(
                permissionName = permission,
                apps = appList.sortedBy { it.name }
            )
        }.sortedByDescending { it.apps.size }
    }

    private suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        val packages = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)

        packages.mapNotNull { packageInfo ->
            try {
                val appInfo = packageManager.getApplicationInfo(packageInfo.packageName, 0)
                val appName = packageManager.getApplicationLabel(appInfo).toString()
                val appIcon = packageManager.getApplicationIcon(appInfo)

                val permissions = packageInfo.requestedPermissions?.toList() ?: emptyList()
                val readablePermissions = permissions.mapNotNull { permission ->
                    getReadablePermissionName(packageManager, permission)
                }

                AppInfo(
                    name = appName,
                    packageName = packageInfo.packageName,
                    icon = appIcon,
                    permissions = readablePermissions
                )
            } catch (_: Exception) {
                null
            }
        }.sortedBy { it.name }
    }

    private fun getReadablePermissionName(packageManager: PackageManager, permission: String): String? {
        return try {
            val permissionInfo = packageManager.getPermissionInfo(permission, 0)
            val label = permissionInfo.loadLabel(packageManager).toString()
            if (label.isNotEmpty() && label != permission) {
                label
            } else {
                // Format the permission name to be more readable
                permission.substringAfterLast(".").replace("_", " ")
                    .split(" ").joinToString(" ") { word ->
                        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    }
            }
        } catch (_: Exception) {
            permission.substringAfterLast(".").replace("_", " ")
                .split(" ").joinToString(" ") { word ->
                    word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                }
        }
    }
}