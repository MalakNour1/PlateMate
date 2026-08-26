package com.example.platemate.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Holds the app-wide dark/light mode preference so any screen's toggle
 * button affects the whole app, not just itself.
 *
 * This starts as an in-memory default (false = light). If you want the
 * choice to survive an app restart, back this with a DataStore
 * Preferences file instead of a plain StateFlow -- read the saved value
 * in init {} and write it inside toggleTheme().
 */
class ThemeViewModel : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }
}