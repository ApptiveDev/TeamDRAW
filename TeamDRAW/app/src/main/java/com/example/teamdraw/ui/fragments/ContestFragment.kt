package com.example.teamdraw.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.teamdraw.R
import com.example.teamdraw.adapters.ContestRVAdapter
import com.example.teamdraw.databinding.FragmentContestBinding
import com.example.teamdraw.viewmodels.ContestsViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContestFragment : Fragment() {

    lateinit var binding: FragmentContestBinding
    private val viewModel: ContestsViewModel by viewModels()

    private val mAdapter by lazy {
        ContestRVAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContestBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        readDatabase()
        setRecyclerView()

        val viewSpinner = binding.viewSpinner
        ArrayAdapter.createFromResource(
            requireNotNull(context),
            R.array.spinner_view_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            viewSpinner.adapter = adapter
        }

        val sortSpinner = binding.sortSpinner
        ArrayAdapter.createFromResource(
            requireNotNull(context),
            R.array.spinner_sort_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sortSpinner.adapter = adapter
        }

        binding.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.text) {
                        "프로젝트" -> viewModel.clickProject()
                        "공모전" -> viewModel.clickContest()
                        else -> viewModel.clickAll()
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            }
        )
        binding.tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.ripple))

        return binding.root
    }

    private fun setRecyclerView() {
        binding.contestRv.adapter = mAdapter
        binding.contestRv.layoutManager = GridLayoutManager(context, 2)
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            viewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setList(database)
                }
            }
        }
    }
}