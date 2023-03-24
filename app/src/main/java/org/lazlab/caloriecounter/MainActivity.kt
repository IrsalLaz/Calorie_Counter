package org.lazlab.caloriecounter

import android.content.Intent
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

        binding.calculateButton.setOnClickListener { calculateBmi() }

        binding.clearButton.setOnClickListener{ clearInput() }

    }

    fun calculateBmi() {
        val weight = binding.weightInput.text.toString()
        if (TextUtils.isEmpty(weight)) {
            Toast.makeText(this, R.string.weight_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val height = binding.heightInput.text.toString()
        if (TextUtils.isEmpty(height)) {
            Toast.makeText(this, R.string.height_invalid, Toast.LENGTH_LONG).show()
            return
        }
        val heightCm = height.toFloat() / 100

        val selectdId = binding.genderRadioGroup.checkedRadioButtonId
        if (selectdId == -1) {
            Toast.makeText(this, R.string.gender_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val isMale = selectdId == R.id.manRadioButton
        val bmi = weight.toFloat() / (heightCm * heightCm)
        val category = getCategory(bmi, isMale)

        //send score & category to bmiActivity
        val scoreBmi = getString(R.string.bmi_x, bmi)
        val categoryBmi = getString(R.string.category_x, category)


        val intent = Intent(this, BmiActivity::class.java).also {
            it.putExtra("EXTRA_SCORE", scoreBmi)
            it.putExtra("EXTRA_CATEGORY", categoryBmi)
            startActivity(it)
        }
    }

    fun clearInput(){
        //resetting input field
        binding.heightInput.text?.clear()
        binding.weightInput.text?.clear()
        binding.genderRadioGroup.clearCheck()
    }

    fun getCategory(bmi: Float, isMale: Boolean): String {
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