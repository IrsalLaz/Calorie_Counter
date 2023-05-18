package org.lazlab.caloriecounter.ui.calculate

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.lazlab.caloriecounter.BmiActivity
import org.lazlab.caloriecounter.R
import org.lazlab.caloriecounter.databinding.FragmentCalculateBinding
import org.lazlab.caloriecounter.db.PersonDb
import org.lazlab.caloriecounter.model.Category
import org.lazlab.caloriecounter.model.Results

class CalculateFragment : Fragment() {

    private lateinit var binding: FragmentCalculateBinding

    private val viewModel: CalculateViewModel by lazy {
        val db = PersonDb.getInstance(requireContext())
        val factory = CalculateViewModelFactory(db.dao)
        ViewModelProvider(this, factory)[CalculateViewModel::class.java]

//        ViewModelProvider(this)[CalculateViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCalculateBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //calculate BMI & BMR
        binding.calculateButton.setOnClickListener { calculate() }

        //get BMI & BMR Score
        viewModel.getBmiBmrScore().observe(requireActivity()) { showBmiResult(it) }

        //clear form
        binding.clearButton.setOnClickListener { clearInput() }

    }

    private fun showBmiResult(result: Results?) {
        binding.result.visibility = View.VISIBLE

        if (result == null) return
        binding.categoryTextView.text =
            getString(R.string.category_x, getCategoryLabel(result.category))
        binding.scoreTextView.text = getString(R.string.bmi_x, result.bmi)
        binding.calorieTextView.text = getString(R.string.bmr_x, result.bmr)
    }

    private fun getCategoryLabel(category: Category): String {
        //convert from Category to string
        val stringRes = when (category) {
            Category.KURUS -> R.string.underweight
            Category.IDEAL -> R.string.ideal
            Category.GEMUK -> R.string.overweight
            Category.OBESITAS -> R.string.obese
        }
        return getString(stringRes)
    }


    private fun calculate() {

        Log.e("SEEME", "calculate done")

        val age = binding.ageInput.text.toString()
        if (TextUtils.isEmpty(age)) {
            Toast.makeText(context, R.string.age_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val height = binding.heightInput.text.toString()
        if (TextUtils.isEmpty(height)) {
            Toast.makeText(context, R.string.height_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val weight = binding.weightInput.text.toString()
        if (TextUtils.isEmpty(weight)) {
            Toast.makeText(context, R.string.weight_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val selectId = binding.genderRadioGroup.checkedRadioButtonId
        if (selectId == -1) {
            Toast.makeText(context, R.string.gender_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val selectActivityLevel = binding.activityRadioGroup.checkedRadioButtonId
        if (selectActivityLevel == -1) {
            Toast.makeText(context, R.string.activity_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val isMale = selectId == R.id.manRadioButton

        //CALCULATE BMI & BMR

        //unDirect calculate(use Calculate.kt)
        viewModel.calculate(
            weight.toFloat(),
            height.toFloat(),
            age.toFloat(),
            isMale,
            dailyActivity(selectActivityLevel)
        )

        //direct calculate
//        viewModel.calculateBmiBmr(
//            weight.toFloat(),
//            height.toFloat(),
//            age.toFloat(),
//            isMale,
//            dailyActivity(selectActivityLevel)
//        )
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