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

        binding.calculateButton.setOnClickListener { calculate() }

        binding.clearButton.setOnClickListener { clearInput() }
    }

    private fun calculate() {
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

        val selectId = binding.genderRadioGroup.checkedRadioButtonId
        if (selectId == -1) {
            Toast.makeText(this, R.string.gender_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val age = binding.ageInput.text.toString()
        if (TextUtils.isEmpty(age)) {
            Toast.makeText(this, R.string.age_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val selectActivityLevel = binding.activityRadioGroup.checkedRadioButtonId
        if (selectActivityLevel == -1) {
            Toast.makeText(this, R.string.activity_invalid, Toast.LENGTH_LONG).show()
            return
        }

        //CALCULATE BMI
        val isMale = selectId == R.id.manRadioButton
        val bmi = weight.toFloat() / (heightCm * heightCm)
        val category = getCategory(bmi, isMale)

        //store & send score & category to bmiActivity
        val scoreBmi = getString(R.string.bmi_x, bmi)
        val categoryBmi = getString(R.string.category_x, category)

        //CALCULATE BMR
        val bmr= getBmr(isMale, weight.toFloat(), height.toFloat(), age.toFloat(), dailyActivity(selectActivityLevel))

        openActivity(scoreBmi, categoryBmi, bmr.toString())
    }

    private fun getBmr(isMale: Boolean, weight: Float, height: Float, age: Float, dailyActivity: Float): String{
        val bmr: Float = if (isMale){
            ((88.4f + 13.4f * weight) + (4.8f * height) - (5.68f * age)) * dailyActivity
        } else{
            ((447.6f + 9.25f * weight) + (3.10f * height) - (4.33f * age)) * dailyActivity
        }

        return  bmr.toString()
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

    private fun dailyActivity(selectActivityLevel: Int) : Float{
        val index : Float = when (selectActivityLevel) {
            R.id.oftenRadioButton -> {
                1.4f
            }
            R.id.rarelyRadioButton -> {
                1.3f
            }
            else -> {
                1.2f
            }
        }
        return index
    }

    private fun clearInput() {
        //clear input field
        binding.ageInput.text?.clear()
        binding.heightInput.text?.clear()
        binding.weightInput.text?.clear()
        binding.genderRadioGroup.clearCheck()
        binding.activityRadioGroup.clearCheck()
    }

    private fun openActivity(score: String, category: String, calorie: String) {
        val intent = Intent(this, BmiActivity::class.java).also {
            it.putExtra("EXTRA_SCORE", score)
            it.putExtra("EXTRA_CATEGORY", category)
            it.putExtra("EXTRA_CALORIE", calorie)
            startActivity(it)
        }
    }
}