package dev.lume.launcher

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import dev.lume.launcher.ui.LumeLauncherApp

class MainActivity : ComponentActivity() {
    private var refreshToken by mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LumeLauncherApp(
                refreshToken = refreshToken,
                onRefresh = { refreshToken += 1 },
                onOpenHomeSettings = ::openHomeSettings
            )
        }
    }

    override fun onResume() {
        super.onResume()
        // Atualiza a lista ao voltar de outro aplicativo ou das configurações do sistema.
        refreshToken += 1
    }

    private fun openHomeSettings() {
        try {
            startActivity(Intent(Settings.ACTION_HOME_SETTINGS))
        } catch (_: ActivityNotFoundException) {
            startActivity(Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS))
        }
    }
}
