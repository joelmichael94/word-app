package com.joel.wordapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.joel.wordapp.MyApplication
import com.joel.wordapp.R
import com.joel.wordapp.databinding.FragmentAddWordBinding
import com.joel.wordapp.data.models.Word
import com.joel.wordapp.ui.viewModels.DetailsViewModel
import com.joel.wordapp.ui.viewModels.EditWordViewModel

// Fragment/View bound to the Add Word UI
class EditWordFragment : Fragment() {
    private lateinit var binding: FragmentAddWordBinding

    // accessing the corresponding viewModel functions
    private val viewModel: EditWordViewModel by viewModels {
        EditWordViewModel.Provider((requireActivity().applicationContext as MyApplication).wordRepo)
    }

    // accessing a separate viewModel for different functions
    private val detailsViewModel: DetailsViewModel by viewModels {
        DetailsViewModel.Provider((requireActivity().applicationContext as MyApplication).wordRepo)
    }

    // declaration of global variables
    var status = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // defining the .xml file to bind this fragment to
        binding = FragmentAddWordBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // accessing navigation argument properties to be accessed (from mainNavGraph)
        val navArgs: EditWordFragmentArgs by navArgs()

        // function to pass id into to get a single data that will be displayed in this fragment
        detailsViewModel.getWordById(navArgs.id)

        // after getting the data, fill the various corresponding input fields with the single data information (for editing)
        detailsViewModel.word.observe(viewLifecycleOwner) {
            status = it.status
            binding.run {
                etAddTitle.setText(it.title)
                etAddMeaning.setText(it.meaning)
                etAddSynonyms.setText(it.synonym)
                etAddDetails.setText(it.details)

                // set the header for this fragment
                tvAddWord.setText(R.string.edit_word)
            }
        }

        // getting values from the edit text, checking that all edit text values are valid, and submitting the values to be added into the RoomDatabase(RD)
        binding.fabAddWord.setOnClickListener {
            val id = navArgs.id
            val title = binding.etAddTitle.text.toString()
            val meaning = binding.etAddMeaning.text.toString()
            val synonyms = binding.etAddSynonyms.text.toString()
            val details = binding.etAddDetails.text.toString()

            if (validate(title, meaning, synonyms, details)) {
                val word = Word(id, title, meaning, synonyms, details, status)
                viewModel.updateWord(id, word)

                // sends information to the MainFragment to refresh the view
                val bundle = Bundle()
                bundle.putBoolean("refresh", true)
                setFragmentResult("from_edit_word", bundle)
                NavHostFragment.findNavController(this).popBackStack()
            } else {
                // alert for the user if any input is incomplete
                Snackbar.make(
                    binding.root,
                    "Title, Meaning, Synonyms and Details are all required",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        // button to return to the previous screen
        binding.fabAddWordBack.setOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }
    }

    // function to check all the input fields
    private fun validate(vararg list: String): Boolean {
        for (field in list) {
            if (field.isEmpty()) {
                return false
            }
        }
        return true
    }
}