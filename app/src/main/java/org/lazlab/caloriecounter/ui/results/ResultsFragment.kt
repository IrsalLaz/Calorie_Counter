package org.lazlab.caloriecounter.ui.results

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.lazlab.caloriecounter.MainAdapter
import org.lazlab.caloriecounter.model.Meals
import org.lazlab.caloriecounter.R
import org.lazlab.caloriecounter.databinding.FragmentResultsBinding
import org.lazlab.caloriecounter.model.Category

class ResultsFragment : Fragment() {

    private lateinit var binding: FragmentResultsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentResultsBinding.inflate(layoutInflater, container, false)

        //set recyclerView
        with(binding.mealRecyclerView) {
            adapter = MainAdapter(getMealsData())
            setHasFixedSize(true)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Log.i("TESTLOG", "Hello onViewCreated reached")

//        val bmi = args.bmi
//        val bmr = args.bmr
//        val category = args.category

        //Show passed data
//        showBmiResult(bmi, bmr, category)
    }


    private fun showBmiResult(bmi: Float, bmr: Float, category: String) {

        binding.categoryTextView.text =
            getString(R.string.category_x, category)
        binding.scoreTextView.text = getString(R.string.bmi_x, bmi)
        binding.calorieTextView.text = getString(R.string.bmr_x, bmr)
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

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = FragmentResultsBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        with(binding.mealRecyclerView){
//            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
//            adapter = MainAdapter(getData())
//            setHasFixedSize(true)
//        }
//
//        val bmiScore =  intent.getStringExtra("EXTRA_SCORE")
//        val bmiCategory =  intent.getStringExtra("EXTRA_CATEGORY")
//        val calorie =  intent.getStringExtra("EXTRA_CALORIE")
//
//        binding.scoreTextView.apply {
//            text = bmiScore
//        }
//
//        binding.categoryTextView.apply {
//            text = bmiCategory
//        }
//
//        binding.calorieTextView.apply {
//            text = calorie
//        }
//
//    }

    private fun getMealsData(): List<Meals> {
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