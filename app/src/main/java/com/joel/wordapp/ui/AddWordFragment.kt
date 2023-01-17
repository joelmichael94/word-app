package com.joel.wordapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.joel.wordapp.MainActivity
import com.joel.wordapp.MyApplication
import com.joel.wordapp.R
import com.joel.wordapp.databinding.FragmentAddWordBinding
import com.joel.wordapp.data.models.Word
import com.joel.wordapp.viewModels.AddWordViewModel

// Fragment/View bound to the Add Word UI
class AddWordFragment : Fragment() {
    private lateinit var binding: FragmentAddWordBinding

    // accessing the corresponding viewModel functions
    private val viewModel: AddWordViewModel by viewModels {
        AddWordViewModel.Provider((requireActivity().applicationContext as MyApplication).wordRepo)
    }

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

        // setting the fragment HEADER text
        binding.tvAddWord.setText(R.string.add_word)

        // getting values from the edit text, checking that all edit text values are valid, and submitting the values to be added into the RoomDatabase(RD)
        binding.fabAddWord.setOnClickListener {
            val title = binding.etAddTitle.text.toString()
            val meaning = binding.etAddMeaning.text.toString()
            val synonyms = binding.etAddSynonyms.text.toString()
            val details = binding.etAddDetails.text.toString()

            if (validate(title, meaning, synonyms, details)) {
                viewModel.addWord(Word(null, title, meaning, synonyms, details))

                // sends information to the MainFragment to refresh the view
                val bundle = Bundle()
                bundle.putBoolean("refresh", true)
                setFragmentResult("from_add_word", bundle)
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