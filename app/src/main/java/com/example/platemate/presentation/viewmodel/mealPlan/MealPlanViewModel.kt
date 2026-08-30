package com.example.platemate.presentation.viewmodel.mealPlan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.platemate.domain.model.MealPlan
import com.example.platemate.domain.model.Recipe
import com.example.platemate.domain.repository.MealPlanRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MealPlanViewModel(private val repository: MealPlanRepository) : ViewModel() {

    val mealPlans: StateFlow<List<MealPlan>> = repository.observeMealPlan()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch { repository.ensureWeeklyRandomFill() }
    }

    fun assignMeal(day: String, recipe: Recipe) {
        viewModelScope.launch { repository.assignManually(day, recipe) }
    }
}