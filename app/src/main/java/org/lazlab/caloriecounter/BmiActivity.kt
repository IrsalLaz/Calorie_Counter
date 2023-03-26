package org.lazlab.caloriecounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.lazlab.caloriecounter.databinding.ActivityBmiBinding

class BmiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBmiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.mealRecyclerView){
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = MainAdapter(getData())
            setHasFixedSize(true)
        }

        val bmiScore =  intent.getStringExtra("EXTRA_SCORE")
        val bmiCategory =  intent.getStringExtra("EXTRA_CATEGORY")
        val calorie =  intent.getStringExtra("EXTRA_CALORIE")

        binding.scoreTextView.apply {
            text = bmiScore
        }

        binding.categoryTextView.apply {
            text = bmiCategory
        }

        binding.calorieTextView.apply {
            text = calorie
        }

    }

    private fun getData(): List<Meals> {
        return listOf(
            Meals("Gado-gado:", 300.0, R.mipmap.gadogado),
            Meals("Nasi goreng sayuran", 400.0, R.mipmap.nasigoreng),
            Meals("Pepes ikan", 400.0, R.mipmap.pepesikan),
            Meals("Sayur lodeh", 250.0, R.mipmap.sayurlodeh),
            Meals("Sate ayam", 300.0, R.mipmap.sateayam),
            Meals("Soto ayam", 300.0, R.mipmap.sotoayam),
            Meals("Sayur asam", 150.0, R.mipmap.sayurasem),
            Meals("Bubur ayam", 300.0, R.mipmap.buburayam),
        )
    }
}