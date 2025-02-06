package com.rpsouza.planner.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.rpsouza.planner.data.database.entity.PlannerActivityEntity
import kotlinx.coroutines.flow.Flow
import com.rpsouza.planner.data.database.DatabaseConstants.TABLE_PLANNER_ACTIVITY

@Dao
interface PlannerActivityDao {

    @Query("SELECT * FROM $TABLE_PLANNER_ACTIVITY ORDER BY is_completed AND datetime")
    fun getAll(): Flow<List<PlannerActivityEntity>>

    @Query("SELECT * FROM $TABLE_PLANNER_ACTIVITY WHERE uuid = :uuid")
    fun getByUuid(uuid: String): Flow<PlannerActivityEntity>

    @Query("UPDATE $TABLE_PLANNER_ACTIVITY SET is_completed = :isCompleted WHERE uuid = :uuid")
    fun updateIsCompletedByUuid(uuid: String, isCompleted: Boolean)

    @Update
    fun update(plannerActivityEntity: PlannerActivityEntity)

    @Query("DELETE FROM $TABLE_PLANNER_ACTIVITY WHERE uuid = :uuid")
    fun deleteByUuid(uuid: String)

}