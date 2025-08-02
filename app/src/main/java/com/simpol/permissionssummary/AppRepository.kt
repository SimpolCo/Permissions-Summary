package com.simpol.permissionssummary

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {
    private val importantPermissions = setOf(
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.ACCESS_COARSE_LOCATION",
        "android.permission.CAMERA",
        "android.permission.RECORD_AUDIO",
        "android.permission.READ_CONTACTS",
        "android.permission.WRITE_CONTACTS",
        "android.permission.READ_PHONE_STATE",
        "android.permission.CALL_PHONE",
        "android.permission.READ_SMS",
        "android.permission.RECEIVE_SMS",
        "android.permission.SEND_SMS",
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.POST_NOTIFICATIONS"
    )


    suspend fun getPermissionGroups(filterState: FilterState = FilterState()): List<PermissionGroup> = withContext(Dispatchers.IO) {
        val apps = getInstalledApps().filter { app ->
            app.packageName !in filterState.hiddenApps
        }

        val permissionToAppsMap = mutableMapOf<String, MutableList<AppInfo>>()

        // Group apps by permissions
        apps.forEach { app ->
            app.permissions.forEach { permission ->
                if (permission !in filterState.hiddenPermissions) {
                    permissionToAppsMap.getOrPut(permission) { mutableListOf() }.add(app)
                }
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

    suspend fun getAllPermissions(): List<String> = withContext(Dispatchers.IO) {
        val apps = getInstalledApps()
        val allPermissions = mutableSetOf<String>()

        apps.forEach { app ->
            allPermissions.addAll(app.permissions)
        }

        allPermissions.toList().sorted()
    }

    suspend fun getAllApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        getInstalledApps()
    }

    private suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        val packages = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)

        packages.mapNotNull { packageInfo ->
            try {
                val appInfo = packageManager.getApplicationInfo(packageInfo.packageName, 0)

                // 🚫 Exclude system apps
                if ((appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0) return@mapNotNull null

                val appName = packageManager.getApplicationLabel(appInfo).toString()
                val appIcon = packageManager.getApplicationIcon(appInfo)

                val grantedPermissions = mutableListOf<String>()
                val requestedPermissions = packageInfo.requestedPermissions
                val requestedPermissionsFlags = packageInfo.requestedPermissionsFlags

                if (requestedPermissions != null && requestedPermissionsFlags != null) {
                    for (i in requestedPermissions.indices) {
                        val permission = requestedPermissions[i]

                        val isGranted = requestedPermissionsFlags[i] and PackageInfo.REQUESTED_PERMISSION_GRANTED != 0

                        // Only proceed if granted and dangerous
                        if (isGranted) {
                            try {
                                val permissionInfo = packageManager.getPermissionInfo(permission, 0)

                                val isDangerous = permissionInfo.protectionLevel and PermissionInfo.PROTECTION_DANGEROUS != 0
                                if (isDangerous && permission in importantPermissions) {
                                    grantedPermissions.add(permission)
                                }
                            } catch (_: Exception) {
                                // Silently skip
                            }
                        }
                    }
                }

                if (grantedPermissions.isEmpty()) return@mapNotNull null

                val readablePermissions = grantedPermissions.mapNotNull {
                    getReadablePermissionName(packageManager, it)
                }

                AppInfo(
                    name = appName,
                    packageName = packageInfo.packageName,
                    icon = appIcon,
                    permissions = readablePermissions
                )
            } catch (e: Exception) {
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
        } catch (e: Exception) {
            permission.substringAfterLast(".").replace("_", " ")
                .split(" ").joinToString(" ") { word ->
                    word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                }
        }
    }
}