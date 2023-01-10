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

class AddWordFragment : Fragment() {
    private lateinit var binding: FragmentAddWordBinding
    private val viewModel: AddWordViewModel by viewModels {
        AddWordViewModel.Provider((requireActivity().applicationContext as MyApplication).wordRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddWordBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvAddWord.setText(R.string.add_word)

        binding.fabAddWord.setOnClickListener {
            val title = binding.etAddTitle.text.toString()
            val meaning = binding.etAddMeaning.text.toString()
            val synonyms = binding.etAddSynonyms.text.toString()
            val details = binding.etAddDetails.text.toString()

            if (validate(title, meaning, synonyms, details)) {
                viewModel.addWord(Word(null, title, meaning, synonyms, details))
                val bundle = Bundle()
                bundle.putBoolean("refresh", true)
                setFragmentResult("from_add_word", bundle)
                NavHostFragment.findNavController(this).popBackStack()
            } else {
                Snackbar.make(
                    binding.root,
                    "Title, Meaning, Synonyms and Details are all required",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        binding.fabAddWordBack.setOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }
    }

    private fun validate(vararg list: String): Boolean {
        for (field in list) {
            if (field.isEmpty()) {
                return false
            }
        }
        return true
    }
}