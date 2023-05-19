package org.lazlab.caloriecounter.ui.meal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.lazlab.caloriecounter.databinding.FragmentAddMealsBinding

class MealsFragment : Fragment() {

    private lateinit var binding: FragmentAddMealsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddMealsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mealName = binding.mealNameInput.text.toString()
        val calorie = binding.mealCalorieInput.text.toString()

        //todo: input meal info to database

        binding.clearMealButton.setOnClickListener { clearInput() }
    }

    private fun clearInput() {
        binding.mealNameInput.text?.clear()
        binding.mealCalorieInput.text?.clear()
    }
}