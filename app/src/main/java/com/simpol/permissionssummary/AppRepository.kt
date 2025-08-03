package com.simpol.permissionssummary

import android.content.Context
import android.content.pm.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {

    // manual permissions grouping
    private val permissionMapping: Map<String, String> =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            mapOf(
                // Camera
                android.Manifest.permission.CAMERA to "Camera",

                // Microphone
                android.Manifest.permission.RECORD_AUDIO to "Microphone",

                // Location
                android.Manifest.permission.ACCESS_FINE_LOCATION to "Location",
                android.Manifest.permission.ACCESS_COARSE_LOCATION to "Location",

                // Media (Android 13+)
                android.Manifest.permission.READ_MEDIA_IMAGES to "Storage (Photos and Videos)",
                android.Manifest.permission.READ_MEDIA_VIDEO to "Storage (Photos and Videos)",
                android.Manifest.permission.READ_MEDIA_AUDIO to "Storage (Music and Sound)",

                // Contacts
                android.Manifest.permission.READ_CONTACTS to "Contacts",
                android.Manifest.permission.WRITE_CONTACTS to "Contacts",
                android.Manifest.permission.GET_ACCOUNTS to "Contacts",

                // Calendar
                android.Manifest.permission.READ_CALENDAR to "Calendar",
                android.Manifest.permission.WRITE_CALENDAR to "Calendar",

                // Call Log
                android.Manifest.permission.READ_CALL_LOG to "Call Log",
                android.Manifest.permission.WRITE_CALL_LOG to "Call Log",
                android.Manifest.permission.PROCESS_OUTGOING_CALLS to "Call Log",

                // Phone
                android.Manifest.permission.READ_PHONE_STATE to "Phone",
                android.Manifest.permission.READ_PHONE_NUMBERS to "Phone",
                android.Manifest.permission.CALL_PHONE to "Phone",
                android.Manifest.permission.ANSWER_PHONE_CALLS to "Phone",
                android.Manifest.permission.ADD_VOICEMAIL to "Phone",
                android.Manifest.permission.USE_SIP to "Phone",
                android.Manifest.permission.ACCEPT_HANDOVER to "Phone",

                // SMS
                android.Manifest.permission.SEND_SMS to "SMS",
                android.Manifest.permission.RECEIVE_SMS to "SMS",
                android.Manifest.permission.READ_SMS to "SMS",
                android.Manifest.permission.RECEIVE_WAP_PUSH to "SMS",
                android.Manifest.permission.RECEIVE_MMS to "SMS",

                // Sensors
                android.Manifest.permission.BODY_SENSORS to "Sensors"
            )
        } else {
            mapOf(
                // Camera
                android.Manifest.permission.CAMERA to "Camera",

                // Microphone
                android.Manifest.permission.RECORD_AUDIO to "Microphone",

                // Location
                android.Manifest.permission.ACCESS_FINE_LOCATION to "Location",
                android.Manifest.permission.ACCESS_COARSE_LOCATION to "Location",

                // Legacy storage
                android.Manifest.permission.READ_EXTERNAL_STORAGE to "Storage",
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE to "Storage",

                // Contacts
                android.Manifest.permission.READ_CONTACTS to "Contacts",
                android.Manifest.permission.WRITE_CONTACTS to "Contacts",
                android.Manifest.permission.GET_ACCOUNTS to "Contacts",

                // Calendar
                android.Manifest.permission.READ_CALENDAR to "Calendar",
                android.Manifest.permission.WRITE_CALENDAR to "Calendar",

                // Call Log
                android.Manifest.permission.READ_CALL_LOG to "Call Log",
                android.Manifest.permission.WRITE_CALL_LOG to "Call Log",
                android.Manifest.permission.PROCESS_OUTGOING_CALLS to "Call Log",

                // Phone
                android.Manifest.permission.READ_PHONE_STATE to "Phone",
                android.Manifest.permission.READ_PHONE_NUMBERS to "Phone",
                android.Manifest.permission.CALL_PHONE to "Phone",
                android.Manifest.permission.ANSWER_PHONE_CALLS to "Phone",
                android.Manifest.permission.ADD_VOICEMAIL to "Phone",
                android.Manifest.permission.USE_SIP to "Phone",
                android.Manifest.permission.ACCEPT_HANDOVER to "Phone",

                // SMS
                android.Manifest.permission.SEND_SMS to "SMS",
                android.Manifest.permission.RECEIVE_SMS to "SMS",
                android.Manifest.permission.READ_SMS to "SMS",
                android.Manifest.permission.RECEIVE_WAP_PUSH to "SMS",
                android.Manifest.permission.RECEIVE_MMS to "SMS",

                // Sensors
                android.Manifest.permission.BODY_SENSORS to "Sensors"
            )
        }


    // custom icons
    private val groupIcons: Map<String, Int> = mapOf(
        "Camera" to R.drawable.ic_camera,
        "Microphone" to R.drawable.ic_microphone,
        "Storage (Photos and Videos)" to R.drawable.ic_photos,
        "Storage (Music and Sound)" to R.drawable.ic_music,
        "Storage" to R.drawable.ic_storage,
        "Location" to R.drawable.ic_location,
        "Contacts" to R.drawable.ic_contacts,
        "Calendar" to R.drawable.ic_calendar,
        "Call Log" to R.drawable.ic_calllog,
        "Phone" to R.drawable.ic_phone,
        "SMS" to R.drawable.ic_sms,
        "Sensors" to R.drawable.ic_sensors
    )


    suspend fun getPermissionGroups(): List<PermissionsGroup> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        // get all apps
        val packages = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)
        // filter and map apps
        val processedPackages =
            packages.mapNotNull { pkg -> createAppInfo(pkg, pm) }.sortedBy { it.name }

        mapAppsToPermissionsGroups(processedPackages)
    }

    private fun createAppInfo(packageInfo: PackageInfo, pm: PackageManager): App? {
        return try {
            val appInfo = pm.getApplicationInfo(packageInfo.packageName, 0)

            // check that app is not system app
            if (appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) return null

            // get granted permissions
            val grantedPermissions = extractGrantedPermissions(packageInfo, pm)
            if (grantedPermissions.isEmpty()) return null

            val permissionObjects = grantedPermissions.mapNotNull { getPermissionObject(it) }

            App(
                name = pm.getApplicationLabel(appInfo).toString(),
                packageName = packageInfo.packageName,
                icon = pm.getApplicationIcon(appInfo),
                permissions = permissionObjects
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
            // bitwise and with REQUESTED_PERMISSION_GRANTED mask to check if granted
            val isGranted = flags[i] and PackageInfo.REQUESTED_PERMISSION_GRANTED != 0
            if (!isGranted) continue

            try {
                val info = pm.getPermissionInfo(permission, 0)

                // check if permission is dangerous
                val isDangerous = info.protectionLevel and PermissionInfo.PROTECTION_DANGEROUS != 0

                if (isDangerous) {
                    result.add(permission)
                }
            } catch (_: Exception) {
                // Ignore missing permission info
            }
        }

        return result
    }

    private fun getPermissionObject(permission: String): String? {
        val group = permissionMapping[permission] ?: return null
        return group
    }

    fun mapAppsToPermissionsGroups(apps: List<App>): List<PermissionsGroup> {
        // Temporary map for building group data
        val groupMap = mutableMapOf<String, MutableList<Pair<App, String>>>()

        for (app in apps) {
            for (perm in app.permissions) {
                val group = perm // Already grouped
                groupMap.getOrPut(group) { mutableListOf() }.add(app to perm)
            }
        }

        return groupMap.mapNotNull { (groupName, appPermPairs) ->
            val iconResId = groupIcons[groupName] ?: return@mapNotNull null
            val groupIcon = context.getDrawable(iconResId) ?: return@mapNotNull null
            val appsInGroup = appPermPairs.map { it.first }.distinct()

            PermissionsGroup(
                groupName = groupName,
                groupIcon = groupIcon,
                apps = appsInGroup
            )
        }
    }

}
