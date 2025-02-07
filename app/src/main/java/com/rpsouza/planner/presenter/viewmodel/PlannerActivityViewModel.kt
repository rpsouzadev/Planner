package com.rpsouza.planner.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rpsouza.planner.core.di.MainServiceLocator
import com.rpsouza.planner.core.di.MainServiceLocator.ioDispatcher
import com.rpsouza.planner.core.di.MainServiceLocator.mainDispatcher
import com.rpsouza.planner.data.datasource.PlannerActivityLocalDataSource
import com.rpsouza.planner.domain.model.PlannerActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.UUID

class PlannerActivityViewModel : ViewModel() {
    private val _activities: MutableStateFlow<List<PlannerActivity>> = MutableStateFlow(emptyList())
    val activities: StateFlow<List<PlannerActivity>> = _activities.asStateFlow()

    val plannerActivityLocalDataSource: PlannerActivityLocalDataSource by lazy {
        MainServiceLocator.plannerActivityLocalDataSource
    }

    fun fetchActivities() {
        viewModelScope.launch {
            launch {
                plannerActivityLocalDataSource.plannerActivities
                    .flowOn(ioDispatcher)
                    .collect { activities ->
                        withContext(mainDispatcher) {
                            _activities.value = activities
                        }
                    }
            }

            launch {
                delay(3_000)
                insert(name = "Jantar", datetime = Calendar.getInstance().timeInMillis)
                delay(3_000)
                insert(name = "Estudar inglês", datetime = Calendar.getInstance().timeInMillis)
                delay(3_000)
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_MONTH, 3)
                insert(name = "Ir para a praia", datetime = calendar.timeInMillis)
            }
        }
    }

    fun insert(name: String, datetime: Long) {
        viewModelScope.launch {
            val plannerActivity = PlannerActivity(
                uuid = UUID.randomUUID().toString(),
                name = name,
                datetime = datetime,
                isCompleted = false
            )

            withContext(ioDispatcher) {
                plannerActivityLocalDataSource.insert(plannerActivity)
            }
        }
    }

    fun update(updatedPlannerActivity: PlannerActivity) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                plannerActivityLocalDataSource.update(updatedPlannerActivity)
            }
        }
    }

    fun updateIsCompleted(uuid: String, isCompleted: Boolean) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                plannerActivityLocalDataSource.updateIsCompletedByUuid(
                    uuid,
                    isCompleted
                )
            }
        }
    }

    fun deleteByUuid(uuid: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                plannerActivityLocalDataSource.deleteByUuid(uuid)
            }
        }
    }
}