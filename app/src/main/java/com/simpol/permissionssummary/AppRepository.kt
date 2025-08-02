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

    suspend fun getPermissionGroups(filterState: FilterState = FilterState()): List<PermissionGroup> =
        withContext(Dispatchers.IO) {
            val apps = getInstalledApps().filterNot { it.packageName in filterState.hiddenApps }

            val permissionToAppsMap = mutableMapOf<String, MutableList<AppInfo>>()

            apps.forEach { app ->
                app.permissions
                    .filterNot { it in filterState.hiddenPermissions }
                    .forEach { permission ->
                        permissionToAppsMap.getOrPut(permission) { mutableListOf() }.add(app)
                    }
            }

            permissionToAppsMap.map { (permission, apps) ->
                PermissionGroup(
                    permissionName = permission,
                    apps = apps.sortedBy { it.name }
                )
            }.sortedByDescending { it.apps.size }
        }

    suspend fun getAllPermissions(): List<String> = withContext(Dispatchers.IO) {
        getInstalledApps()
            .flatMap { it.permissions }
            .toSet()
            .sorted()
    }

    suspend fun getAllApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        getInstalledApps()
    }

    private suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val packages = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)

        packages.mapNotNull { pkg -> createAppInfo(pkg, pm) }
            .sortedBy { it.name }
    }

    private fun createAppInfo(packageInfo: PackageInfo, pm: PackageManager): AppInfo? {
        return try {
            val appInfo = pm.getApplicationInfo(packageInfo.packageName, 0)

            // Skip system apps
            if (appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) return null

            val grantedPermissions = extractGrantedPermissions(packageInfo, pm)
            if (grantedPermissions.isEmpty()) return null

            val readablePermissions =
                grantedPermissions.mapNotNull { getReadablePermissionName(pm, it) }

            AppInfo(
                name = pm.getApplicationLabel(appInfo).toString(),
                packageName = packageInfo.packageName,
                icon = pm.getApplicationIcon(appInfo),
                permissions = readablePermissions
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun extractGrantedPermissions(pkg: PackageInfo, pm: PackageManager): List<String> {
        val result = mutableListOf<String>()
        val permissions = pkg.requestedPermissions ?: return emptyList()
        val flags = pkg.requestedPermissionsFlags ?: return emptyList()

        for (i in permissions.indices) {
            val permission = permissions[i]
            val isGranted = flags[i] and PackageInfo.REQUESTED_PERMISSION_GRANTED != 0

            if (!isGranted) continue

            try {
                val info = pm.getPermissionInfo(permission, 0)
                val isDangerous = info.protectionLevel and PermissionInfo.PROTECTION_DANGEROUS != 0

                if (isDangerous && permission in importantPermissions) {
                    result.add(permission)
                }
            } catch (_: Exception) {
                // Ignore missing permission info
            }
        }

        return result
    }

    private fun getReadablePermissionName(pm: PackageManager, permission: String): String? {
        return try {
            val label = pm.getPermissionInfo(permission, 0).loadLabel(pm).toString()
            if (label.isNotBlank() && label != permission) label else formatPermissionName(
                permission
            )
        } catch (_: Exception) {
            formatPermissionName(permission)
        }
    }

    private fun formatPermissionName(permission: String): String {
        return permission.substringAfterLast(".")
            .replace("_", " ")
            .split(" ")
            .joinToString(" ") { it.replaceFirstChar(Char::titlecase) }
    }
}
