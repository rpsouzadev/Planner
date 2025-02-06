package com.rpsouza.planner.domain.mapper

import com.rpsouza.planner.data.database.entity.PlannerActivityEntity
import com.rpsouza.planner.domain.model.PlannerActivity

fun PlannerActivityEntity.toDomain(): PlannerActivity = PlannerActivity(
    uuid = this.uuid,
    name = this.name,
    datetime = this.datetime,
    isCompleted = this.isCompleted
)

fun PlannerActivity.toEntity(id: Int): PlannerActivityEntity = PlannerActivityEntity(
    id = id,
    uuid = this.uuid,
    name = this.name,
    datetime = this.datetime,
    isCompleted = this.isCompleted
)
