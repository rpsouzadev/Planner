package com.rpsouza.planner.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rpsouza.planner.data.database.DatabaseConstants.TABLE_PLANNER_ACTIVITY

@Entity(tableName = TABLE_PLANNER_ACTIVITY)
data class PlannerActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val uuid: String,
    val name: String,
    val datetime: Long,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean
)
