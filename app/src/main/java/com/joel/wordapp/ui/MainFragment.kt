package com.joel.wordapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.joel.wordapp.adapters.ViewPagerAdapter
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
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ViewPagerAdapter(
            listOf(newWordFragment, completedWordFragment),
            requireActivity().supportFragmentManager, lifecycle
        )
        binding.vpMain.adapter = adapter
        TabLayoutMediator(binding.tlMain, binding.vpMain) { tab, position ->
            tab.text = when (position) {
                0 -> "New Words"
                else -> "Completed Words"
            }
        }.attach()
    }
}