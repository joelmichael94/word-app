package com.joel.wordapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.joel.wordapp.MainActivity
import com.joel.wordapp.adapters.WordAdapter
import com.joel.wordapp.databinding.FragmentNewWordBinding
import com.joel.wordapp.viewModels.CompletedWordViewModel
import com.joel.wordapp.viewModels.MainViewModel

class CompletedWordFragment private constructor() : Fragment() {
    private lateinit var binding: FragmentNewWordBinding
    private lateinit var adapter: WordAdapter
    private val viewModel: CompletedWordViewModel by viewModels {
        CompletedWordViewModel.Provider((requireActivity() as MainActivity).wordRepo)
    }
    private val mainViewModel: MainViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewWordBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()

        binding.fabNewWord.setOnClickListener {
            val action = MainFragmentDirections.actionMainToAddWord()
            NavHostFragment.findNavController(this).navigate(action)
        }

        viewModel.words.observe(viewLifecycleOwner) {
            adapter.setWords(it)
            if (it.isNullOrEmpty()) {
                binding.ivEmpty.isVisible = true
                binding.tvEmpty.isVisible = true
            } else {
                binding.ivEmpty.isVisible = false
                binding.tvEmpty.isVisible = false
            }
        }

        mainViewModel.refreshCompletedWords.observe(viewLifecycleOwner) {
            if (it) {
                refresh("")
                mainViewModel.shouldRefreshWords(false)
            }
        }
    }

    fun refresh(str: String) {
        lifecycleScope.launchWhenResumed {
            viewModel.getWords(str)
        }
    }

    fun setupAdapter() {
        val layoutManager = LinearLayoutManager(requireContext())
        adapter = WordAdapter(emptyList()) {
            val action = MainFragmentDirections.actionMainToDetails(it.id!!)
            NavHostFragment.findNavController(this).navigate(action)
        }
        binding.rvNewWord.layoutManager = layoutManager
        binding.rvNewWord.adapter = adapter
    }

    companion object {
        private var completedWordFragmentInstance: CompletedWordFragment? = null
        fun getInstance(): CompletedWordFragment {
            if (completedWordFragmentInstance == null) {
                completedWordFragmentInstance = CompletedWordFragment()
            }
            return completedWordFragmentInstance!!
        }
    }
}