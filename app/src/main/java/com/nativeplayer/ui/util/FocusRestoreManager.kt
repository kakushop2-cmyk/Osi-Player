package com.nativeplayer.ui.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged

/**
 * Zentraler Manager zur Speicherung und Wiederherstellung des Fokus
 * wenn aus dem Vollbildmodus zurückgekehrt wird.
 */
object FocusRestoreManager {
    private val lastFocusMap = mutableMapOf<String, FocusInfo>()

    data class FocusInfo(
        val itemKey: String,
        val itemIndex: Int = -1
    )

    fun saveFocus(screenRoute: String, itemKey: String, itemIndex: Int = -1) {
        lastFocusMap[screenRoute] = FocusInfo(itemKey, itemIndex)
    }

    fun getLastFocus(screenRoute: String): FocusInfo? = lastFocusMap[screenRoute]

    fun clearFocus(screenRoute: String) {
        lastFocusMap.remove(screenRoute)
    }

    fun clearAll() {
        lastFocusMap.clear()
    }
}

/**
 * Modifier, der den Fokus-Status eines Items speichert wenn es fokussiert wird.
 * Verwendet die Item-Key als Identifier.
 */
fun Modifier.focusRestorableItem(
    screenRoute: String,
    itemKey: String,
    itemIndex: Int = -1,
    focusRequester: FocusRequester? = null
): Modifier = this.then(
    Modifier.onFocusChanged { focusState ->
        if (focusState.isFocused) {
            FocusRestoreManager.saveFocus(screenRoute, itemKey, itemIndex)
        }
    }.let { modifier ->
        if (focusRequester != null) {
            modifier.focusRequester(focusRequester)
        } else {
            modifier
        }
    }
)

/**
 * Composable, das den Fokus auf ein Item in einer LazyList wiederherstellt.
 * Muss im gleichen Composable wie die LazyList verwendet werden.
 *
 * @param screenRoute Die Route des Screens (z.B. Screen.Live.route)
 * @param listState Der LazyListState der Liste
 * @param items Die Items der Liste (zum Finden des Index)
 * @param keyExtractor Funktion, die den Key aus einem Item extrahiert
 * @param focusRequesters Map von Item-Key zu FocusRequester
 */
@Composable
fun <T> RestoreLazyListFocus(
    screenRoute: String,
    listState: LazyListState,
    items: List<T>,
    keyExtractor: (T) -> String,
    focusRequesters: Map<String, FocusRequester> = emptyMap()
) {
    val lastFocus = remember(screenRoute) { FocusRestoreManager.getLastFocus(screenRoute) }

    LaunchedEffect(lastFocus, items) {
        lastFocus?.let { focusInfo ->
            // Versuche zuerst über den Index zu scrollen
            if (focusInfo.itemIndex >= 0 && focusInfo.itemIndex < items.size) {
                listState.scrollToItem(focusInfo.itemIndex)
                // Warte einen Frame, bis das Item gerendert ist
                kotlinx.coroutines.delay(100)
                focusRequesters[focusInfo.itemKey]?.requestFocus()
            } else {
                // Suche das Item über den Key
                val index = items.indexOfFirst { keyExtractor(it) == focusInfo.itemKey }
                if (index >= 0) {
                    listState.scrollToItem(index)
                    kotlinx.coroutines.delay(100)
                    focusRequesters[focusInfo.itemKey]?.requestFocus()
                }
            }
        }
    }
}

/**
 * Composable, das den Fokus auf ein Item in einem LazyGrid wiederherstellt.
 */
@Composable
fun <T> RestoreLazyGridFocus(
    screenRoute: String,
    gridState: LazyGridState,
    items: List<T>,
    keyExtractor: (T) -> String,
    focusRequesters: Map<String, FocusRequester> = emptyMap()
) {
    val lastFocus = remember(screenRoute) { FocusRestoreManager.getLastFocus(screenRoute) }

    LaunchedEffect(lastFocus, items) {
        lastFocus?.let { focusInfo ->
            val index = if (focusInfo.itemIndex >= 0 && focusInfo.itemIndex < items.size) {
                focusInfo.itemIndex
            } else {
                items.indexOfFirst { keyExtractor(it) == focusInfo.itemKey }
            }

            if (index >= 0) {
                gridState.scrollToItem(index)
                kotlinx.coroutines.delay(100)
                focusRequesters[focusInfo.itemKey]?.requestFocus()
            }
        }
    }
}

/**
 * Erstellt FocusRequester für ein Item und speichert sie in einer Map.
 * Verwendet rememberSaveable, um die Requester über Konfigurationsänderungen zu erhalten.
 */
@Composable
fun <T> rememberFocusRequesterMap(
    items: List<T>,
    keyExtractor: (T) -> String
): Map<String, FocusRequester> {
    return remember(items) {
        items.associate { keyExtractor(it) to FocusRequester() }
    }
}

/**
 * Hilfsfunktion, die den Fokus auf ein bestimmtes Element setzt.
 * Wird verwendet, wenn ein Screen zum ersten Mal angezeigt wird
 * oder wenn aus dem Vollbild zurückgekehrt wird.
 */
@Composable
fun RequestFocusOnItem(
    focusRequester: FocusRequester,
    enabled: Boolean = true
) {
    LaunchedEffect(enabled) {
        if (enabled) {
            kotlinx.coroutines.delay(100)
            try {
                focusRequester.requestFocus()
            } catch (_: Exception) {
                // FocusRequester kann fehlschlagen, wenn das Element nicht sichtbar ist
            }
        }
    }
}

/**
 * Hilfsfunktion, die den Fokus auf das erste Item einer Liste setzt.
 * Nützlich als Fallback, wenn kein gespeicherter Fokus vorhanden ist.
 */
@Composable
fun RequestFocusOnFirstItem(
    listState: LazyListState,
    focusRequester: FocusRequester,
    itemCount: Int,
    enabled: Boolean = true
) {
    LaunchedEffect(enabled, itemCount) {
        if (enabled && itemCount > 0) {
            kotlinx.coroutines.delay(150)
            try {
                listState.scrollToItem(0)
                focusRequester.requestFocus()
            } catch (_: Exception) {
                // Ignorieren
            }
        }
    }
}
