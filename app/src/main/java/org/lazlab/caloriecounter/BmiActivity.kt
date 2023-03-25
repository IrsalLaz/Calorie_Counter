package org.lazlab.caloriecounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.lazlab.caloriecounter.databinding.ActivityBmiBinding

class BmiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBmiBinding
    private lateinit var recyclerView: RecyclerView
    private var adapter: MainAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        initRecyclerView()

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
            Meals("Ayam Teriyaki", 300.0),
            Meals("Nasi Liwet", 600.0),
        )
    }

    private fun initRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(getData())
        recyclerView.adapter = adapter
    }
}