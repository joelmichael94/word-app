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
import com.joel.wordapp.data.models.SortBy
import com.joel.wordapp.data.models.SortKey
import com.joel.wordapp.data.models.SortOrder
import com.joel.wordapp.utils.Dropdown
import com.joel.wordapp.utils.StorageService
import com.joel.wordapp.viewModels.MainViewModel
import com.joel.wordapp.viewModels.NewWordViewModel
import kotlinx.coroutines.flow.asSharedFlow

// Fragment/View bound to the New Word UI
class NewWordFragment private constructor() : Fragment() {
    private lateinit var binding: FragmentNewWordBinding
    private lateinit var adapter: WordAdapter

    // accessing the corresponding viewModel functions
    private val viewModel: NewWordViewModel by viewModels {
        NewWordViewModel.Provider(
            (requireActivity().applicationContext as MyApplication).wordRepo,
            (requireActivity().applicationContext as MyApplication).storageService
        )
    }

    // accessing a separate viewModel for different functions
    private val mainViewModel: MainViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    // declaration of global variables
    private var search: String = ""
    private var order: String = ""
    private var type: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // defining the .xml file to bind this fragment to
        binding = FragmentNewWordBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // binding the sortDialog "Modal" .xml to be used in this fragment
        val dialogBinding = ItemSortDialogLayoutBinding.inflate(layoutInflater)

        // setting a style type for the Dialog "Modal" to follow
        val alertDialog = Dialog(requireContext(), R.style.DataBinding_AlertDialog)

        // create layout for RecyclerView(RV), generate the data(from RoomDB) to display in the RV, hold navigation function
        setupAdapter()

        // checks the sortDialog CHECKED radio buttons for sorting purpose
        viewModel.sortBy.observe(viewLifecycleOwner) {
            dialogBinding.rbTitle.isChecked = it == SortBy.TITLE.name
            dialogBinding.rbDate.isChecked = it == SortBy.DATE.name
        }

        // checks the sortDialog CHECKED radio buttons for sorting purpose
        viewModel.sortOrder.observe(viewLifecycleOwner) {
            dialogBinding.rbAscending.isChecked = it == SortOrder.ASCENDING.name
            dialogBinding.rbDescending.isChecked = it == SortOrder.DESCENDING.name
        }

//        binding.fabToDropdown.setOnClickListener {
//            val action = MainFragmentDirections.actionMainToDropdown()
//            NavHostFragment.findNavController(this).navigate(action)
//        }

        // refreshes the view/fragment when screen swiped down for refreshing and resets the search input
        binding.srlRefresh.setOnRefreshListener {
            viewModel.onRefresh()
            binding.etSearch.setText("")
        }

        // checks that the swipeRefresh has finished running its function (after the delay of 3000ms) and sets the isRefreshing property to false to prevent infinite refreshing animation
        viewModel.swipeRefreshLayoutFinished.asLiveData().observe(viewLifecycleOwner) {
            binding.srlRefresh.isRefreshing = false
        }

        // navigation button to Add Word Fragment / View
        binding.fabNewWord.setOnClickListener {
            val action = MainFragmentDirections.actionMainToAddWord()
            NavHostFragment.findNavController(this).navigate(action)
        }

        // Element to display if no data in fragment or hide if data exists in fragment
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

        // function to refresh the view when data in fragment is changed
        mainViewModel.refreshWords.observe(viewLifecycleOwner) {
            if (it) {
                refresh("")
                mainViewModel.shouldRefreshWords(false)
            }
        }

        // function to refresh the data displayed in the view when search/filtering the data by its title
        binding.ibSearch.setOnClickListener {
            search = binding.etSearch.text.toString()
            refresh(search)
        }

        // function to check the sortDialog functions && style
        binding.ibSort.setOnClickListener {
            alertDialog.window?.setBackgroundDrawableResource(R.color.app_1)

            // checks to make sure only 1 radio button can be checked for each category
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

            // gets the values from the selected radio buttons and passes them to the appropriate functions
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

    // function to be called when you want to re-fetch the data being displayed in the fragment
    fun refresh(str: String) {
        lifecycleScope.launchWhenResumed {
            viewModel.getWords(str)
        }
    }

    // creates the layout to be used by the RecyclerView(hold the list of data), generates the data to be put into the RV, and holds the navigation function.
    fun setupAdapter() {
        val layoutManager = LinearLayoutManager(requireContext())
        adapter = WordAdapter(emptyList()) {
            val action = MainFragmentDirections.actionMainToDetails(it.id!!)
            NavHostFragment.findNavController(this).navigate(action)
        }
        binding.rvNewWord.adapter = adapter
        binding.rvNewWord.layoutManager = layoutManager
    }

    // companion object to be called by the MainFragment to store THIS fragment into the TabLayout/ViewPager2 in the MainFragment.
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