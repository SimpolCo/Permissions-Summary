package com.simpol.permissionssummary

data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: android.graphics.drawable.Drawable?,
    val permissions: List<String>
)

data class PermissionGroup(
    val permissionName: String,
    val apps: List<AppInfo>
)