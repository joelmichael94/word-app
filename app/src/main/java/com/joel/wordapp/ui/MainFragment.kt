package com.joel.wordapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.joel.wordapp.adapters.ViewPagerAdapter
import com.joel.wordapp.adapters.WordAdapter
import com.joel.wordapp.databinding.FragmentMainBinding
import com.joel.wordapp.viewModels.MainViewModel

// Fragment/View bound to the MainActivity UI
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    // accessing the corresponding viewModel functions
    private val viewModel: MainViewModel by viewModels()

    // fetching other fragments to be used in this Fragment
    private val newWordFragment = NewWordFragment.getInstance()
    private val completedWordFragment = CompletedWordFragment.getInstance()
    var currentPage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // defining the .xml file to bind this fragment to
        binding = FragmentMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
//        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // creates the adapter that is going to be bound to this fragments viewPager (TabLayout)
        // consists of 2 Tabs, NewWords and CompletedWords
        val adapter = ViewPagerAdapter(
            listOf(newWordFragment, completedWordFragment),
//            requireActivity().supportFragmentManager
            childFragmentManager, lifecycle
        )

        // bind the above declared adapter to this fragments' viewPager element
        binding.vpMain.adapter = adapter

        // handles the tab navigation animation and titles for each tab
        TabLayoutMediator(binding.tlMain, binding.vpMain) { tab, position ->
            tab.text = when (position) {
                0 -> "New Words"
                else -> "Completed Words"
            }
        }.attach()

        // handles the fragment to display when tabs are switched
        binding.vpMain.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }
        })

        // an event listener to re-fetch and refresh the displayed data when the requestKey is produced from another fragment
        setFragmentResultListener("from_add_word") { _, result ->
            val refresh = result.getBoolean("refresh")
            viewModel.shouldRefreshWords(refresh)
        }

        // an event listener to re-fetch and refresh the displayed data when the requestKey is produced from another fragment
        setFragmentResultListener("from_details") { _, result ->
            val refresh = result.getBoolean("refresh")
            viewModel.shouldRefreshWords(refresh)
            viewModel.shouldRefreshCompletedWords(refresh)
        }

        // an event listener to re-fetch and refresh the displayed data when the requestKey is produced from another fragment
        setFragmentResultListener("from_edit_word") { _, result ->
            val refresh = result.getBoolean("refresh")
            viewModel.shouldRefreshWords(refresh)
            viewModel.shouldRefreshCompletedWords(refresh)
        }
    }
}