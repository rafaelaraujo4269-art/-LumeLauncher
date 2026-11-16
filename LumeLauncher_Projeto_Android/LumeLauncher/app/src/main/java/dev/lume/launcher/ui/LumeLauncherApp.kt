package dev.lume.launcher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import dev.lume.launcher.data.FavoritesRepository
import dev.lume.launcher.data.InstalledApp
import dev.lume.launcher.data.LauncherAppsRepository
import dev.lume.launcher.ui.components.AppTile
import dev.lume.launcher.ui.theme.LumeAccent
import dev.lume.launcher.ui.theme.LumeBackground
import dev.lume.launcher.ui.theme.LumeBackgroundDeep
import dev.lume.launcher.ui.theme.LumeMuted
import dev.lume.launcher.ui.theme.LumeSurface
import dev.lume.launcher.ui.theme.LumeText
import dev.lume.launcher.ui.theme.LumeTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LumeLauncherApp(
    refreshToken: Int,
    onRefresh: () -> Unit,
    onOpenHomeSettings: () -> Unit
) {
    val context = LocalContext.current
    val appsRepository = remember { LauncherAppsRepository(context.applicationContext) }
    val favoritesRepository = remember { FavoritesRepository(context.applicationContext) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var apps by remember { mutableStateOf<List<InstalledApp>>(emptyList()) }
    var favoriteKeys by remember { mutableStateOf(favoritesRepository.load()) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var showSettings by remember { mutableStateOf(false) }

    LaunchedEffect(refreshToken) {
        isLoading = true
        try {
            apps = withContext(Dispatchers.IO) { appsRepository.loadApps() }
        } catch (_: Exception) {
            snackbarHostState.showSnackbar("Não foi possível carregar a lista de aplicativos.")
        } finally {
            isLoading = false
        }
    }

    var currentDateTime by remember { mutableStateOf(LocalDateTime.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(30_000)
            currentDateTime = LocalDateTime.now()
        }
    }

    val visibleApps = remember(apps, searchQuery) {
        val term = searchQuery.trim()
        if (term.isEmpty()) apps
        else apps.filter { it.label.contains(term, ignoreCase = true) }
    }
    val favoriteApps = remember(visibleApps, favoriteKeys) {
        visibleApps.filter { it.key in favoriteKeys }
    }

    fun toggleFavorite(app: InstalledApp) {
        val updated = if (app.key in favoriteKeys) {
            favoriteKeys - app.key
        } else {
            favoriteKeys + app.key
        }
        favoriteKeys = updated
        favoritesRepository.save(updated)
        scope.launch {
            snackbarHostState.showSnackbar(
                if (app.key in updated) "${app.label} foi adicionado aos favoritos."
                else "${app.label} foi removido dos favoritos."
            )
        }
    }

    LumeTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(LumeBackground, LumeBackgroundDeep)
                        )
                    )
                    .padding(contentPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {
                    HeaderBar(
                        onSettings = { showSettings = true },
                        onRefresh = onRefresh
                    )

                    Spacer(Modifier.height(14.dp))

                    GreetingBlock(dateTime = currentDateTime)

                    Spacer(Modifier.height(18.dp))

                    SearchField(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onClear = { searchQuery = "" }
                    )

                    Spacer(Modifier.height(12.dp))

                    when {
                        isLoading -> LoadingState(modifier = Modifier.weight(1f))
                        apps.isEmpty() -> EmptyState(
                            title = "Nenhum aplicativo encontrado",
                            message = "Quando houver apps iniciáveis no perfil atual, eles aparecerão aqui.",
                            modifier = Modifier.weight(1f)
                        )
                        else -> AppsGrid(
                            visibleApps = visibleApps,
                            favoriteApps = favoriteApps,
                            favoriteKeys = favoriteKeys,
                            onLaunch = { app ->
                                runCatching { appsRepository.launch(app) }
                                    .onFailure {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Não foi possível abrir ${app.label}.")
                                        }
                                    }
                            },
                            onToggleFavorite = ::toggleFavorite,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Text(
                        text = "SEGURE UM APLICATIVO PARA FAVORITAR",
                        color = LumeMuted.copy(alpha = 0.65f),
                        fontSize = 9.sp,
                        letterSpacing = 1.1.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 10.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }

    if (showSettings) {
        AlertDialog(
            onDismissRequest = { showSettings = false },
            containerColor = LumeSurface,
            icon = {
                Icon(Icons.Filled.Home, contentDescription = null, tint = LumeAccent)
            },
            title = {
                Text("Usar Lume como tela inicial", color = LumeText)
            },
            text = {
                Text(
                    "Abra as configurações de aplicativos padrão do Android e escolha Lume Launcher como aplicativo de início. Você poderá trocar essa escolha depois.",
                    color = LumeMuted
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showSettings = false
                    onOpenHomeSettings()
                }) {
                    Text("Abrir configurações", color = LumeAccent)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSettings = false }) {
                    Text("Agora não", color = LumeMuted)
                }
            }
        )
    }
}

@Composable
private fun HeaderBar(
    onSettings: () -> Unit,
    onRefresh: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "L U M E",
                color = LumeAccent,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
            Text(
                text = "SEU ESPAÇO DIGITAL",
                color = LumeMuted,
                fontSize = 9.sp,
                letterSpacing = 1.4.sp
            )
        }
        IconButton(onClick = onRefresh) {
            Icon(Icons.Filled.Refresh, contentDescription = "Atualizar aplicativos", tint = LumeMuted)
        }
        IconButton(onClick = onSettings) {
            Icon(Icons.Filled.Settings, contentDescription = "Configurações", tint = LumeMuted)
        }
    }
}

@Composable
private fun GreetingBlock(dateTime: LocalDateTime) {
    val greeting = when (dateTime.hour) {
        in 5..11 -> "Bom dia."
        in 12..17 -> "Boa tarde."
        else -> "Boa noite."
    }
    val dateText = remember(dateTime.toLocalDate()) {
        DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale("pt", "BR"))
            .format(dateTime.toLocalDate())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("pt", "BR")) else it.toString() }
    }
    val timeText = remember(dateTime) {
        DateTimeFormatter.ofPattern("HH:mm").format(dateTime)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = greeting,
                color = LumeText,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = dateText,
                color = LumeMuted,
                fontSize = 12.sp
            )
        }
        Text(
            text = timeText,
            color = LumeText,
            fontSize = 34.sp,
            fontWeight = FontWeight.Light,
            letterSpacing = (-1).sp
        )
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = { Text("Buscar aplicativos", color = LumeMuted) },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = null, tint = LumeAccent)
        },
        trailingIcon = if (query.isNotEmpty()) {
            {
                IconButton(onClick = onClear) {
                    Icon(Icons.Filled.Close, contentDescription = "Limpar busca", tint = LumeMuted)
                }
            }
        } else null,
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = LumeText,
            unfocusedTextColor = LumeText,
            focusedContainerColor = LumeSurface,
            unfocusedContainerColor = LumeSurface,
            focusedBorderColor = LumeAccent.copy(alpha = 0.65f),
            unfocusedBorderColor = Color.Transparent,
            cursorColor = LumeAccent
        )
    )
}

@Composable
private fun AppsGrid(
    visibleApps: List<InstalledApp>,
    favoriteApps: List<InstalledApp>,
    favoriteKeys: Set<String>,
    onLaunch: (InstalledApp) -> Unit,
    onToggleFavorite: (InstalledApp) -> Unit,
    modifier: Modifier = Modifier
) {
    if (visibleApps.isEmpty()) {
        EmptyState(
            title = "Nada por aqui",
            message = "Tente outro nome ou limpe a busca para ver todos os aplicativos.",
            modifier = modifier
        )
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 76.dp),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = 5.dp, bottom = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        if (favoriteApps.isNotEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                SectionHeading(title = "Favoritos", subtitle = "Acesso rápido")
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    contentPadding = PaddingValues(bottom = 7.dp)
                ) {
                    items(favoriteApps.size, key = { favoriteApps[it].key }) { index ->
                        AppTile(
                            app = favoriteApps[index],
                            isFavorite = true,
                            onClick = { onLaunch(favoriteApps[index]) },
                            onLongClick = { onToggleFavorite(favoriteApps[index]) },
                            modifier = Modifier.width(78.dp)
                        )
                    }
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            SectionHeading(
                title = if (favoriteApps.isNotEmpty()) "Todos os aplicativos" else "Aplicativos",
                subtitle = "${visibleApps.size} disponíveis"
            )
        }

        items(visibleApps, key = { it.key }) { app ->
            AppTile(
                app = app,
                isFavorite = app.key in favoriteKeys,
                onClick = { onLaunch(app) },
                onLongClick = { onToggleFavorite(app) }
            )
        }
    }
}

@Composable
private fun SectionHeading(title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = LumeText,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )
        Text(text = subtitle, color = LumeMuted, fontSize = 10.sp)
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = LumeAccent, strokeWidth = 2.dp)
    }
}

@Composable
private fun EmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            color = LumeSurface,
            shape = CircleShape,
            modifier = Modifier.size(64.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Apps, contentDescription = null, tint = LumeAccent, modifier = Modifier.size(29.dp))
            }
        }
        Spacer(Modifier.height(14.dp))
        Text(title, color = LumeText, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(5.dp))
        Text(
            message,
            color = LumeMuted,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
