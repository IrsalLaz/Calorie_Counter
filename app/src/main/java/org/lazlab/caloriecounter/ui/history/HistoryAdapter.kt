package org.lazlab.caloriecounter.ui.history

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.lazlab.caloriecounter.R
import org.lazlab.caloriecounter.databinding.ListHistoryBinding
import org.lazlab.caloriecounter.db.PersonEntity
import org.lazlab.caloriecounter.model.Category
import org.lazlab.caloriecounter.model.calculateBmiBmr
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter :
    ListAdapter<PersonEntity, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<PersonEntity>() {
                override fun areItemsTheSame(
                    oldData: PersonEntity, newData: PersonEntity
                ): Boolean {
                    return oldData.id == newData.id
                }

                override fun areContentsTheSame(
                    oldItem: PersonEntity,
                    newItem: PersonEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    class ViewHolder(
        private val binding: ListHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormatter =
            SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

        fun bind(item: PersonEntity) = with(binding) {
            val results = item.calculateBmiBmr()
            categoryIcon.text = results.category.toString().subSequence(0, 1)

            val colorRes = when (results.category) {
                Category.KURUS -> R.color.underweight
                Category.IDEAL -> R.color.ideal
                Category.GEMUK -> R.color.overweight
                else -> R.color.obese
            }

            categoryTextView.text = results.category.toString()

            val circleBg = categoryIcon.background as GradientDrawable
            circleBg.setColor(ContextCompat.getColor(root.context, colorRes))

            bmiTextView.text = root.context.getString(R.string.bmi_x, results.bmi)

            calorieTextView.text = root.context.getString(R.string.bmr_x, results.bmr)

            dateTextView.text = dateFormatter.format((Date(item.date)))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListHistoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}