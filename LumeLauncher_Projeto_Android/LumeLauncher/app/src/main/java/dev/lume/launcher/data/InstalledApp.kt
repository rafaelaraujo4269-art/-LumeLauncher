package dev.lume.launcher.data

import android.content.ComponentName
import android.graphics.drawable.Drawable
import android.os.UserHandle

/** Representa uma atividade que o Android disponibiliza como aplicativo iniciável. */
data class InstalledApp(
    val label: String,
    val componentName: ComponentName,
    val user: UserHandle,
    val icon: Drawable
) {
    // O componente é estável entre reinicializações e identifica o app/atividade.
    val key: String
        get() = componentName.flattenToString()
}
