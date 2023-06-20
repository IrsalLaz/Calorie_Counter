package org.lazlab.caloriecounter.ui.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import org.lazlab.caloriecounter.MainAdapter
import org.lazlab.caloriecounter.model.Meals
import org.lazlab.caloriecounter.R
import org.lazlab.caloriecounter.databinding.FragmentResultsBinding
import org.lazlab.caloriecounter.model.Category

class ResultsFragment : Fragment() {

    private lateinit var binding: FragmentResultsBinding

    private val args: ResultsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentResultsBinding.inflate(layoutInflater, container, false)

        //set recyclerView
        with(binding.mealRecyclerView) {
            adapter = MainAdapter(getMealsData())
            setHasFixedSize(true)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        //Show passed data
        showCalorie(categorie = getCategoryLabel(args.categories))
    }

    private fun showCalorie(categorie: String?) {
        binding.resultTextView.text = getString(R.string.category_x, categorie)
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