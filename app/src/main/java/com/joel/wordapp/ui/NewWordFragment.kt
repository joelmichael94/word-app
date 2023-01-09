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
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.joel.wordapp.MainActivity
import com.joel.wordapp.MyApplication
import com.joel.wordapp.R
import com.joel.wordapp.adapters.WordAdapter
import com.joel.wordapp.databinding.FragmentNewWordBinding
import com.joel.wordapp.databinding.ItemSortDialogLayoutBinding
import com.joel.wordapp.models.SortBy
import com.joel.wordapp.models.SortKey
import com.joel.wordapp.models.SortOrder
import com.joel.wordapp.utils.Dropdown
import com.joel.wordapp.utils.StorageService
import com.joel.wordapp.viewModels.MainViewModel
import com.joel.wordapp.viewModels.NewWordViewModel
import kotlinx.coroutines.flow.asSharedFlow

class NewWordFragment private constructor() : Fragment() {
    private lateinit var binding: FragmentNewWordBinding
    private lateinit var adapter: WordAdapter
    private val viewModel: NewWordViewModel by viewModels {
        NewWordViewModel.Provider(
            (requireActivity().applicationContext as MyApplication).wordRepo,
            (requireActivity().applicationContext as MyApplication).storageService
        )
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
        val dialogBinding = ItemSortDialogLayoutBinding.inflate(layoutInflater)
        val alertDialog = Dialog(requireContext(), R.style.DataBinding_AlertDialog)

        setupAdapter()

        viewModel.sortBy.observe(viewLifecycleOwner) {
            dialogBinding.rbTitle.isChecked = it == SortBy.TITLE.name
            dialogBinding.rbDate.isChecked = it == SortBy.DATE.name
        }

        viewModel.sortOrder.observe(viewLifecycleOwner) {
            dialogBinding.rbAscending.isChecked = it == SortOrder.ASCENDING.name
            dialogBinding.rbDescending.isChecked = it == SortOrder.DESCENDING.name
        }

        binding.fabToDropdown.setOnClickListener {
            val action = MainFragmentDirections.actionMainToDropdown()
            NavHostFragment.findNavController(this).navigate(action)
        }

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

        mainViewModel.refreshWords.observe(viewLifecycleOwner) {
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
            alertDialog.window?.setBackgroundDrawableResource(R.color.app_1)
            dialogBinding.rgOrder.setOnCheckedChangeListener { _, id ->
                when (id) {
                    R.id.rb_ascending -> order = SortOrder.ASCENDING.name
                    else -> order = SortOrder.DESCENDING.name
                }
            }
            dialogBinding.rgType.setOnCheckedChangeListener { _, id ->
                when (id) {
                    R.id.rb_title -> type = SortBy.TITLE.name
                    else -> type = SortBy.DATE.name
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
                    viewModel.onChangeSortBy(type)
                    viewModel.onChangeSortOrder(order)
                    viewModel.sortNewWords(search, order, type)
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