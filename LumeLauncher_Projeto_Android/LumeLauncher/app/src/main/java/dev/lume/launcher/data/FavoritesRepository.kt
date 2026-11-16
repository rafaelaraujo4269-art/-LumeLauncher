package dev.lume.launcher.data

import android.content.Context

/** Persistência local e simples dos favoritos do usuário. */
class FavoritesRepository(context: Context) {
    private val preferences = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    fun load(): Set<String> =
        preferences.getStringSet(KEY_FAVORITES, mutableSetOf())?.toSet() ?: emptySet()

    fun save(favorites: Set<String>) {
        preferences.edit()
            .putStringSet(KEY_FAVORITES, favorites.toSet())
            .apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "lume_launcher_preferences"
        const val KEY_FAVORITES = "favorite_components"
    }
}
