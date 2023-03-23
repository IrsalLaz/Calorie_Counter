package org.lazlab.caloriecounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.lazlab.caloriecounter.databinding.ActivityMainBinding

class BmiPage : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    private fun getData(): List<Meals> {
        return listOf(
            Meals("Ayam Teriyaki", 300.0, "ayam", "makanan enak mudah dibuat"),
            Meals("Nasi Liwet", 600.0, "nasi, telur, ayam sewir", "makanan enak mudah dibuat"),
        )
    }
}