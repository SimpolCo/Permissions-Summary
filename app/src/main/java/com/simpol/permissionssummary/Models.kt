package com.simpol.permissionssummary

data class App(
    val name: String,
    val packageName: String,
    val icon: android.graphics.drawable.Drawable?,
    val permissions: List<String>
)

data class PermissionsGroup(
    val groupName: String,
    val groupIcon: android.graphics.drawable.Drawable,
    val apps: List<App>
)
