package com.simpol.permissionssummary

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Intent
import android.net.Uri
import android.provider.Settings


@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(viewModel: AppListViewModel = viewModel()) {
    val permissionGroups by viewModel.permissionsGroups.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Permissions Summary")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            PermissionList(
                modifier = Modifier.padding(innerPadding),
                permissions = permissionGroups
            )
        }
    }
}

@Composable
fun PermissionList(modifier: Modifier = Modifier, permissions: List<PermissionsGroup>) {
    var expandedGroupName by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(permissions) { permissionGroup ->
            PermissionGroupItem(
                permission = permissionGroup,
                expandedGroupName = expandedGroupName,
                onGroupExpand = { groupName -> expandedGroupName = groupName }
            )
        }
    }
}


@Composable
fun PermissionGroupItem(
    permission: PermissionsGroup,
    expandedGroupName: String?,
    onGroupExpand: (String?) -> Unit
) {
    val isExpanded = expandedGroupName == permission.groupName

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onGroupExpand(if (isExpanded) null else permission.groupName)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            PermissionGroupHeader(
                groupName = permission.groupName,
                groupIcon = permission.groupIcon,
                appCount = permission.apps.size,
                expanded = isExpanded,
                onExpandToggle = {
                    onGroupExpand(if (isExpanded) null else permission.groupName)
                }
            )

            if (isExpanded && permission.apps.isNotEmpty()) {
                PermissionGroupContent(apps = permission.apps)
            }
        }
    }
}

@Composable
private fun PermissionGroupHeader(
    groupName: String,
    groupIcon: android.graphics.drawable.Drawable,
    appCount: Int,
    expanded: Boolean,
    onExpandToggle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            bitmap = groupIcon.toBitmap(24, 24).asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = groupName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "$appCount app${if (appCount != 1) "s" else ""} use this permission",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        IconButton(onClick = onExpandToggle) {
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (expanded) "Collapse" else "Expand",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun PermissionGroupContent(apps: List<App>) {
    Spacer(modifier = Modifier.height(12.dp))
    HorizontalDivider(
        Modifier,
        DividerDefaults.Thickness,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    )
    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = "Apps using this permission:",
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.primary
    )

    Spacer(modifier = Modifier.height(8.dp))

    apps.forEach { app ->
        AppItemCompact(app = app)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun AppItemCompact(app: App) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:${app.packageName}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            app.icon?.let { drawable ->
                Image(
                    bitmap = drawable.toBitmap(48, 48).asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = app.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = app.packageName,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
