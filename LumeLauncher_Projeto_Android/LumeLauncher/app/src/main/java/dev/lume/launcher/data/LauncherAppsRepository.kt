package dev.lume.launcher.data

import android.content.Context
import android.content.pm.LauncherApps
import android.os.Process
import java.util.Locale

/** Consulta e inicia aplicativos usando as APIs públicas do Android para launchers. */
class LauncherAppsRepository(context: Context) {
    private val packageName = context.packageName
    private val launcherApps = requireNotNull(
        context.getSystemService(LauncherApps::class.java)
    ) { "LauncherApps não está disponível neste dispositivo." }

    fun loadApps(): List<InstalledApp> {
        return launcherApps
            .getActivityList(null, Process.myUserHandle())
            .asSequence()
            .filter { it.applicationInfo.packageName != packageName }
            .mapNotNull { activity ->
                runCatching {
                    InstalledApp(
                        label = activity.label?.toString()?.trim().orEmpty()
                            .ifBlank { activity.applicationInfo.packageName },
                        componentName = activity.componentName,
                        user = activity.user,
                        icon = activity.getIcon(0)
                    )
                }.getOrNull()
            }
            .sortedBy { it.label.lowercase(Locale.getDefault()) }
            .toList()
    }

    fun launch(app: InstalledApp) {
        launcherApps.startMainActivity(
            app.componentName,
            app.user,
            null,
            null
        )
    }
}
