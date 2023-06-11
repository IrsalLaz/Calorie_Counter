package org.lazlab.caloriecounter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.lazlab.caloriecounter.databinding.ListMealsBinding
import org.lazlab.caloriecounter.model.Meals
import org.lazlab.caloriecounter.network.MealsApi

class MainAdapter() : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val data = mutableListOf<Meals>()

    fun updateData(newData: List<Meals>)
    {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ListMealsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(meal: Meals) = with(binding) {
            mealNameTextView.text = meal.name
            calorieTextView.text = meal.calorie.toString()

            //PROBLEM HERE
            Glide.with(mealImageView.context)
                .load(MealsApi.getMealUrl(meal.imageResId))
                .error(R.drawable.ic_broken_image)
                .into(mealImageView)
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