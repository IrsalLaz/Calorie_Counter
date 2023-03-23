package org.lazlab.caloriecounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import org.lazlab.caloriecounter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        gunakan class MainAdapter.kt pada MainActivity.kt

        binding.calculateButton.setOnClickListener { hitungBmi() }
    }

    private fun getData(): List<Meals> {
        return listOf(
            Meals("Ayam Teriyaki", 300.0, "ayam", "makanan enak mudah dibuat"),
            Meals("Nasi Liwet", 600.0, "nasi, telur, ayam sewir", "makanan enak mudah dibuat"),
        )
    }

    private fun hitungBmi() {
        val berat = binding.weightInput.text.toString()
        if (TextUtils.isEmpty(berat)) {
            Toast.makeText(this, R.string.weight_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val tinggi = binding.heightInput.text.toString()
        if (TextUtils.isEmpty(tinggi)) {
            Toast.makeText(this, R.string.height_invalid, Toast.LENGTH_LONG).show()
            return
        }
        val tinggiCm = tinggi.toFloat() / 100

        val selectdId = binding.genderRadioGroup.checkedRadioButtonId
        if (selectdId == -1) {
            Toast.makeText(this, R.string.gender_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val isMale = selectdId == R.id.manRadioButton
        val bmi = berat.toFloat() / (tinggiCm * tinggiCm)
        val kategori = getCategory(bmi, isMale)

        binding.bmiTextView.text = getString(R.string.bmi_x, bmi)
        binding.kategoriTextView.text = getString(R.string.category_x, kategori)
    }

    private fun getCategory(bmi: Float, isMale: Boolean): String {
        val stringRes = if (isMale) {
            when {
                bmi < 18.5 -> R.string.underweight
                bmi >= 27.0 -> R.string.overweight
                bmi >= 30.0 -> R.string.obese
                else -> R.string.ideal
            }
        } else {
            when {
                bmi < 18.5 -> R.string.underweight
                bmi >= 25.0 -> R.string.overweight
                bmi >= 31.0 -> R.string.obese
                else -> R.string.ideal
            }
        }
        return getString(stringRes)
    }
}