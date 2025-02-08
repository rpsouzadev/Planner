package com.rpsouza.planner.presenter.viewmodel

import java.time.Year

data class NewPlannerActivity(
    val name: String? = null,
    val date: SetDate? = null,
    val time: SetTime? = null
) {
    fun isFilled(): Boolean = !name.isNullOrEmpty() && date != null && time != null
}

data class SetDate(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int
)

data class SetTime(
    val hour: Int,
    val minute: Int
)