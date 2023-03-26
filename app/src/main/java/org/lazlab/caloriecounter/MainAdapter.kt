package org.lazlab.caloriecounter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.lazlab.caloriecounter.databinding.ListMealsBinding

class MainAdapter(private val data: List<Meals>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>(){
    class ViewHolder(
        private val binding: ListMealsBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(meal: Meals) = with(binding){
            mealNameTextView.text = meal.name
            calorieTextView.text= meal.calorie.toString()
            mealImageView.setImageResource(meal.imageResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListMealsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
}