package com.example.platemate.domain.util

import java.util.Calendar

object WeekUtils {
    private const val DAY_MS = 24 * 60 * 60 * 1000L

    // Returns the epoch day (days since 1970-01-01) of the most recent Saturday,
    // since MealPlannerScreen's week starts on Saturday.
    fun currentWeekStartEpochDay(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) // Sunday=1 ... Saturday=7
        val daysSinceSaturday = ((dayOfWeek - Calendar.SATURDAY) + 7) % 7
        cal.add(Calendar.DAY_OF_YEAR, -daysSinceSaturday)

        return cal.timeInMillis / DAY_MS
    }
}