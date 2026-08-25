package com.example.platemate.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.platemate.data.connectivity.NetworkMonitor
import com.example.platemate.domain.repository.RecipeRepository

class RecipeViewModelFactory(
    private val repository: RecipeRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            return RecipeViewModel(repository, networkMonitor) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}