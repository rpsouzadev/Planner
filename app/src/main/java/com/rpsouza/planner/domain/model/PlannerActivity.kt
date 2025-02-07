package com.rpsouza.planner.domain.model

import android.icu.util.Calendar
import com.rpsouza.planner.domain.utils.createCalendarFromTimeInMillis
import com.rpsouza.planner.domain.utils.toPlannerActivityDate
import com.rpsouza.planner.domain.utils.toPlannerActivityDatetime
import com.rpsouza.planner.domain.utils.toPlannerActivityTime

data class PlannerActivity(
    val uuid: String,
    val name: String,
    val datetime: Long,
    val isCompleted: Boolean
) {
    private val datetimeCalendar: Calendar = createCalendarFromTimeInMillis(datetime)
    val dateString = datetimeCalendar.toPlannerActivityDate()
    val timeString = datetimeCalendar.toPlannerActivityTime()
    val datetimeString = datetimeCalendar.toPlannerActivityDatetime()
}
