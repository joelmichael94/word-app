package com.joel.wordapp.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.joel.wordapp.MainActivity
import com.joel.wordapp.R
import com.joel.wordapp.adapters.WordAdapter
import com.joel.wordapp.databinding.FragmentNewWordBinding
import com.joel.wordapp.databinding.ItemSortDialogLayoutBinding
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
    private var search: String = ""
    private var order: String = ""
    private var type: String = ""

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

        binding.srlRefresh.setOnRefreshListener {
            viewModel.onRefresh()
            binding.etSearch.setText("")
        }

        viewModel.swipeRefreshLayoutFinished.asLiveData().observe(viewLifecycleOwner) {
            binding.srlRefresh.isRefreshing = false
        }

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

        binding.ibSearch.setOnClickListener {
            search = binding.etSearch.text.toString()
            refresh(search)
        }

        binding.ibSort.setOnClickListener {
            val dialogBinding = ItemSortDialogLayoutBinding.inflate(layoutInflater)
            val alertDialog = Dialog(requireContext(), R.style.DataBinding_AlertDialog)

            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogBinding.rgOrder.setOnCheckedChangeListener { _, id ->
                when (id) {
                    R.id.rb_ascending -> order = "ascending"
                    else -> order = "descending"
                }
            }
            dialogBinding.rgType.setOnCheckedChangeListener { _, id ->
                when (id) {
                    R.id.rb_title -> type = "title"
                    else -> type = "date"
                }
            }
            alertDialog.setContentView(dialogBinding.root)
            alertDialog.setCancelable(true)
            alertDialog.show()

            dialogBinding.btnSort.setOnClickListener {
                if (dialogBinding.rgOrder.checkedRadioButtonId == -1
                    || dialogBinding.rgType.checkedRadioButtonId == -1
                ) {
                    dialogBinding.tvAlert.isVisible = true
                } else {
                    viewModel.sortCompletedWords(search, order, type)
                    alertDialog.dismiss()
                }
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