package org.lazlab.caloriecounter.ui.results

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import org.lazlab.caloriecounter.MainActivity
import org.lazlab.caloriecounter.MainAdapter
import org.lazlab.caloriecounter.R
import org.lazlab.caloriecounter.databinding.FragmentResultsBinding
import org.lazlab.caloriecounter.model.Category
import org.lazlab.caloriecounter.network.ApiStatus

class ResultsFragment : Fragment() {

    private lateinit var binding: FragmentResultsBinding

    private lateinit var myAdapter: MainAdapter

    private val args: ResultsFragmentArgs by navArgs()

    private val viewModel: ResultViewModel by lazy {
        Log.d("MainViewModel", "Success in RFG")
        ViewModelProvider(this)[ResultViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d("RFG", "onCreateView")

        binding = FragmentResultsBinding.inflate(layoutInflater, container, false)
        myAdapter = MainAdapter()

        //set recyclerView
        with(binding.mealRecyclerView) {
            adapter = myAdapter
            setHasFixedSize(true)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Show passed data
        showCalorie(categorie = getCategoryLabel(args.categories))

        //Show meals data
        viewModel.getData().observe(viewLifecycleOwner) {
            myAdapter.updateData(it)
        }

        viewModel.getStatus().observe(viewLifecycleOwner) {
            updateProgress(it)
        }
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

    private fun updateProgress(status: ApiStatus) {
        when (status) {
            ApiStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            ApiStatus.SUCCESS -> {
                binding.progressBar.visibility = View.GONE

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermission()
                }
            }

            ApiStatus.FAILED -> {
                binding.progressBar.visibility = View.GONE
                binding.networkError.visibility = View.VISIBLE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                MainActivity.PERMISSION_REQUEST_CODE
            )
        }
    }
}