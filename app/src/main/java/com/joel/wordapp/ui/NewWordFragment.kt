package com.joel.wordapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.joel.wordapp.MainActivity
import com.joel.wordapp.adapters.WordAdapter
import com.joel.wordapp.databinding.FragmentNewWordBinding
import com.joel.wordapp.viewModels.MainViewModel
import com.joel.wordapp.viewModels.NewWordViewModel

class NewWordFragment private constructor() : Fragment() {
    private lateinit var binding: FragmentNewWordBinding
    private lateinit var adapter: WordAdapter
    private val viewModel: NewWordViewModel by viewModels {
        NewWordViewModel.Provider((requireActivity() as MainActivity).wordRepo)
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
        }

        mainViewModel.refreshWords.observe(viewLifecycleOwner) {
            if (it) {
                refresh("")
                mainViewModel.shouldRefreshWords(false)
            }
        }
    }

    fun refresh(str: String) {
        viewModel.getWords(str)
    }

    fun setupAdapter() {
        val layoutManager = LinearLayoutManager(requireContext())
        adapter = WordAdapter(emptyList()) {
//            mainfrag, action main to deets
//            val action = MainFragmentDirections.actionMainToDetails(it.id!!)
//            NavHostFragment.findNavController(this).navigate(action)
        }
        binding.rvNewWord.adapter = adapter
        binding.rvNewWord.layoutManager = layoutManager
    }

    companion object {
        private var newWordFragmentInstance: NewWordFragment? = null
        fun getInstance(): NewWordFragment {
            if (newWordFragmentInstance == null) {
                newWordFragmentInstance = NewWordFragment()
            }
            return newWordFragmentInstance!!
        }
    }
}