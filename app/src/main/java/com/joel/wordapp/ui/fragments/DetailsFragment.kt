package com.joel.wordapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joel.wordapp.MyApplication
import com.joel.wordapp.R
import com.joel.wordapp.databinding.FragmentDetailsBinding
import com.joel.wordapp.ui.viewModels.DetailsViewModel

// Fragment / View bound to the Details UI
class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding

    // accessing the corresponding viewModel functions
    val viewModel: DetailsViewModel by viewModels {
        DetailsViewModel.Provider(
            (requireContext().applicationContext as MyApplication).wordRepo
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // defining the .xml file to bind this fragment to
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // accessing navigation argument properties to be accessed (from mainNavGraph)
        val navArgs: DetailsFragmentArgs by navArgs()

        // function to pass id into to get a single data that will be displayed in this fragment
        viewModel.getWordById(navArgs.id)

        // after getting the data, fill the various corresponding text fields with the single data information
        viewModel.word.observe(viewLifecycleOwner) {
            if (it.status) binding.btnCompleteWord.isVisible = false
            binding.run {
                tvTitle.text = it.title
                tvMeaning.text = it.meaning
                tvSynonym.text = it.synonym
                tvDetails.text = it.details
            }
        }

        // passes the id into the function to change the status of the item and re-fetch the data
        binding.btnCompleteWord.setOnClickListener {
            viewModel.changeStatus(navArgs.id)
            val bundle = Bundle()
            bundle.putBoolean("refresh", true)
            setFragmentResult("from_details", bundle)
            NavHostFragment.findNavController(this).popBackStack()
        }

        // button to navigate to editWord fragment
        binding.btnEditWord.setOnClickListener {
            val action = DetailsFragmentDirections.actionDetailsToEditWord(navArgs.id)
            NavHostFragment.findNavController(this).navigate(action)
        }

        // button to delete a word, produces a popup dialog for confirmation of deletion
        binding.fabDeleteWord.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("refresh", true)
            MaterialAlertDialogBuilder(requireContext(), R.style.DataBinding_AlertDialog)
                .setTitle(binding.tvTitle.text).setMessage(binding.tvMeaning.text)
                .setCancelable(true)
                .setPositiveButton("Delete") { _, it ->
                    viewModel.deleteWord(navArgs.id)
                    setFragmentResult("from_details", bundle)
                    NavHostFragment.findNavController(this).popBackStack()
                }.setNegativeButton("Cancel") { _, it -> }
                .show()
        }

        // button to navigate back to previous fragment / view
        binding.fabDetailsBack.setOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }

    }
}