package com.example.platemate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.platemate.domain.repository.RecipeRepository

class RecipeViewModelFactory(private val repository: RecipeRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(RecipeViewModel::class.java))
        {
            return RecipeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")

    }
}