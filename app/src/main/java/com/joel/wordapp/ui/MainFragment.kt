package com.joel.wordapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val newWordFragment = NewWordFragment.getInstance()
    private val completedWordFragment = CompletedWordFragment.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
//        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var currentPage = 0

        val adapter = ViewPagerAdapter(
            listOf(newWordFragment, completedWordFragment),
//            requireActivity().supportFragmentManager
            childFragmentManager, lifecycle
        )

        binding.vpMain.adapter = adapter

        TabLayoutMediator(binding.tlMain, binding.vpMain) { tab, position ->
            tab.text = when (position) {
                0 -> "New Words"
                else -> "Completed Words"
            }
        }.attach()

        binding.vpMain.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }
        })

        setFragmentResultListener("from_add_word") { _, result ->
            val refresh = result.getBoolean("refresh")
            viewModel.shouldRefreshWords(refresh)
        }
    }
}