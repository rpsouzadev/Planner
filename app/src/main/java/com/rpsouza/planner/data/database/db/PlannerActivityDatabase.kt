package com.rpsouza.planner.data.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rpsouza.planner.data.database.dao.PlannerActivityDao
import com.rpsouza.planner.data.database.entity.PlannerActivityEntity

@Database(entities = [PlannerActivityEntity::class], version = 1)
abstract class PlannerActivityDatabase : RoomDatabase() {
    abstract fun plannerActivityDao(): PlannerActivityDao
}