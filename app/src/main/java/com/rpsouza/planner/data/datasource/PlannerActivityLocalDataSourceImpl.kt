package com.rpsouza.planner.data.datasource

import com.rpsouza.planner.data.database.dao.PlannerActivityDao
import com.rpsouza.planner.domain.mapper.toDomain
import com.rpsouza.planner.domain.mapper.toEntity
import com.rpsouza.planner.domain.model.PlannerActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlannerActivityLocalDataSourceImpl(
    private val plannerActivityDao: PlannerActivityDao
): PlannerActivityLocalDataSource {
    override val plannerActivities: Flow<List<PlannerActivity>>
        get() = plannerActivityDao.getAll().map { entities ->
            entities.map { entity -> entity.toDomain() }
        }

    override fun getByUuid(uuid: String): PlannerActivity {
        return plannerActivityDao.getByUuid(uuid).toDomain()
    }

    override fun updateIsCompletedByUuid(uuid: String, isCompleted: Boolean) {
        return plannerActivityDao.updateIsCompletedByUuid(uuid, isCompleted)
    }

    override fun update(plannerActivity: PlannerActivity) {
        val entity = plannerActivityDao.getByUuid(plannerActivity.uuid)
        return plannerActivityDao.update(plannerActivity.toEntity(entity.id))
    }

    override fun deleteByUuid(uuid: String) {
        plannerActivityDao.deleteByUuid(uuid)
    }
}