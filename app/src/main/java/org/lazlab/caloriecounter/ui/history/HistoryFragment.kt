package org.lazlab.caloriecounter.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.lazlab.caloriecounter.R
import org.lazlab.caloriecounter.data.SettingsDataStore
import org.lazlab.caloriecounter.data.dataStore
import org.lazlab.caloriecounter.databinding.FragmentHistoryBinding
import org.lazlab.caloriecounter.db.PersonDb

class HistoryFragment : Fragment() {

    private val layoutDataStore: SettingsDataStore by lazy {
        SettingsDataStore(requireContext().dataStore)
    }

    private val viewModel: HistoryViewModel by lazy {
        val db = PersonDb.getInstance(requireContext())
        val factory = HitungViewModelFactory(db.dao)
        ViewModelProvider(this, factory)[HistoryViewModel::class.java]
    }

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var myAdapter: HistoryAdapter
    private var isLinearLayout = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        myAdapter = HistoryAdapter()
        with(binding.recyclerView) {
            adapter = myAdapter
            setHasFixedSize(true)
        }

        viewModel.data.observe(viewLifecycleOwner) {
            binding.emptyView.visibility = if (it.isEmpty())
                View.VISIBLE else View.GONE
            myAdapter.submitList(it)
        }

        layoutDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner) {
            isLinearLayout = it
            setLayout()
            activity?.invalidateOptionsMenu()
        }
    }

    private fun setLayout() {
        binding.recyclerView.layoutManager = if (isLinearLayout)
            LinearLayoutManager(context)
        else
            GridLayoutManager(context, 2)
    }

    private fun setIcon(menuItem: MenuItem) {
        val iconId = if (isLinearLayout)
            R.drawable.ic_grid_view
        else
            R.drawable.ic_view_list
        menuItem.icon = ContextCompat.getDrawable(requireContext(), iconId)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.history_menu, menu)

        val menuItem = menu.findItem(R.id.menu_switch)
        setIcon(menuItem)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                deleteData()
                return true
            }

            R.id.menu_switch -> {
                lifecycleScope.launch {
                    layoutDataStore.saveLayout(!isLinearLayout, requireContext())
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteData() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.delete_confirmation)
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteData()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
}