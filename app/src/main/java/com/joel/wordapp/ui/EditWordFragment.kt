package com.joel.wordapp.ui

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
import com.joel.wordapp.MainActivity
import com.joel.wordapp.MyApplication
import com.joel.wordapp.R
import com.joel.wordapp.databinding.FragmentAddWordBinding
import com.joel.wordapp.models.Word
import com.joel.wordapp.viewModels.DetailsViewModel
import com.joel.wordapp.viewModels.EditWordViewModel

class EditWordFragment : Fragment() {
    private lateinit var binding: FragmentAddWordBinding
    private val viewModel: EditWordViewModel by viewModels {
        EditWordViewModel.Provider((requireActivity().applicationContext as MyApplication).wordRepo)
    }
    private val detailsViewModel: DetailsViewModel by viewModels {
        DetailsViewModel.Provider((requireActivity().applicationContext as MyApplication).wordRepo)
    }
    var status = false

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

        val navArgs: EditWordFragmentArgs by navArgs()

        detailsViewModel.getWordById(navArgs.id)

        detailsViewModel.word.observe(viewLifecycleOwner) {
            status = it.status
            binding.run {
                etAddTitle.setText(it.title)
                etAddMeaning.setText(it.meaning)
                etAddSynonyms.setText(it.synonym)
                etAddDetails.setText(it.details)
                tvAddWord.setText(R.string.edit_word)
            }
        }

        binding.fabAddWord.setOnClickListener {
            val id = navArgs.id
            val title = binding.etAddTitle.text.toString()
            val meaning = binding.etAddMeaning.text.toString()
            val synonyms = binding.etAddSynonyms.text.toString()
            val details = binding.etAddDetails.text.toString()

            if (validate(title, meaning, synonyms, details)) {
                val word = Word(id, title, meaning, synonyms, details, status)
                viewModel.updateWord(id, word)
                val bundle = Bundle()
                bundle.putBoolean("refresh", true)
                setFragmentResult("from_edit_word", bundle)
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