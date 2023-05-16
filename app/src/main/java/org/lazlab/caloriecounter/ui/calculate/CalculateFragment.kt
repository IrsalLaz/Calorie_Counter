package org.lazlab.caloriecounter.ui.calculate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.lazlab.caloriecounter.BmiActivity
import org.lazlab.caloriecounter.R
import org.lazlab.caloriecounter.databinding.FragmentCalculateBinding
import org.lazlab.caloriecounter.db.PersonDb

class CalculateFragment : Fragment() {

    private lateinit var binding: FragmentCalculateBinding

    private val viewModel: CalculateViewModel by lazy {
        val db = PersonDb.getInstance(requireContext())
        val factory = CalculateViewModelFactory(db.dao)
        ViewModelProvider(this, factory)[CalculateViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCalculateBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCalculateBinding.inflate(layoutInflater)
        binding.calculateButton.setOnClickListener { calculate() }
        binding.clearButton.setOnClickListener { clearInput() }


    }


    private fun calculate() {
        val weight = binding.weightInput.text.toString()
        if (TextUtils.isEmpty(weight)) {
            Toast.makeText(requireContext(), R.string.weight_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val height = binding.heightInput.text.toString()
        if (TextUtils.isEmpty(height)) {
            Toast.makeText(requireContext(), R.string.height_invalid, Toast.LENGTH_LONG).show()
            return
        }
        val heightCm = height.toFloat() / 100

        val selectId = binding.genderRadioGroup.checkedRadioButtonId
        if (selectId == -1) {
            Toast.makeText(requireContext(), R.string.gender_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val age = binding.ageInput.text.toString()
        if (TextUtils.isEmpty(age)) {
            Toast.makeText(requireContext(), R.string.age_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val selectActivityLevel = binding.activityRadioGroup.checkedRadioButtonId
        if (selectActivityLevel == -1) {
            Toast.makeText(requireContext(), R.string.activity_invalid, Toast.LENGTH_LONG).show()
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
        val bmr = getBmr(
            isMale,
            weight.toFloat(),
            height.toFloat(),
            age.toFloat(),
            dailyActivity(selectActivityLevel)
        )

        val calorieIntake = getString(R.string.bmr_x, bmr)
        openActivity(scoreBmi, categoryBmi, calorieIntake)
    }


    private fun getBmr(
        isMale: Boolean,
        weight: Float,
        height: Float,
        age: Float,
        dailyActivity: Float
    ): String {
        val bmr: Float = if (isMale) {
            ((88.4f + 13.4f * weight) + (4.8f * height) - (5.68f * age)) * dailyActivity
        } else {
            ((447.6f + 9.25f * weight) + (3.10f * height) - (4.33f * age)) * dailyActivity
        }

        return bmr.toString()
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

    private fun dailyActivity(selectActivityLevel: Int): Float {
        val index: Float = when (selectActivityLevel) {
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
        val intent = Intent(requireContext(), BmiActivity::class.java).also {
            it.putExtra("EXTRA_SCORE", score)
            it.putExtra("EXTRA_CATEGORY", category)
            it.putExtra("EXTRA_CALORIE", calorie)
            startActivity(it)
        }
    }
}