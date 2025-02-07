package com.rpsouza.planner.presenter.component

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rpsouza.planner.R
import com.rpsouza.planner.databinding.ItemPlannerActivityBinding
import com.rpsouza.planner.domain.model.PlannerActivity

class PlannerActivityAdapter :
    ListAdapter<PlannerActivity, PlannerActivityAdapter.MyViewHolder>(PlannerActivityDiffCallback) {

    object PlannerActivityDiffCallback : DiffUtil.ItemCallback<PlannerActivity>() {
        override fun areItemsTheSame(oldItem: PlannerActivity, newItem: PlannerActivity): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: PlannerActivity, newItem: PlannerActivity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPlannerActivityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val plannerActivity = getItem(position)
        holder.bind(plannerActivity)
    }

    inner class MyViewHolder(private val binding: ItemPlannerActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(plannerActivity: PlannerActivity) {
            with(binding) {
                tvName.text = plannerActivity.name
                tvDateTime.text = plannerActivity.datetimeString
                ivStatus.setImageResource(
                    if (plannerActivity.isCompleted) {
                        ivStatus.setColorFilter(
                            ContextCompat.getColor(
                                binding.root.context,
                                R.color.lime_300
                            )
                        )
                        R.drawable.ic_circle_check
                    } else {
                        ivStatus.clearColorFilter()
                        R.drawable.ic_circle_dashed
                    }
                )
            }
        }
    }
}