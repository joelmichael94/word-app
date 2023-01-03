package com.joel.wordapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joel.wordapp.MyApplication
import com.joel.wordapp.R
import com.joel.wordapp.databinding.FragmentDetailsBinding
import com.joel.wordapp.viewModels.DetailsViewModel

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    val viewModel: DetailsViewModel by viewModels {
        DetailsViewModel.Provider((requireContext().applicationContext as MyApplication).wordRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navArgs: DetailsFragmentArgs by navArgs()

        viewModel.getWordById(navArgs.id)

        viewModel.word.observe(viewLifecycleOwner) {
            if (it.status) binding.btnCompleteWord.isVisible = false
            binding.run {
                tvTitle.text = it.title
                tvMeaning.text = it.meaning
                tvSynonym.text = it.synonym
                tvDetails.text = it.details
            }
        }

        binding.btnCompleteWord.setOnClickListener {
            viewModel.changeStatus(navArgs.id)
            val bundle = Bundle()
            bundle.putBoolean("refresh", true)
            setFragmentResult("from_details", bundle)
            NavHostFragment.findNavController(this).popBackStack()
        }

        binding.btnEditWord.setOnClickListener {
            val action = DetailsFragmentDirections.actionDetailsToEditWord(navArgs.id)
            NavHostFragment.findNavController(this).navigate(action)
        }

        binding.fabDeleteWord.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("refresh", true)
            MaterialAlertDialogBuilder(requireContext(), R.style.DataBinding_AlertDialog)
                .setTitle(binding.tvTitle.text).setMessage(binding.tvMeaning.text)
                .setCancelable(true)
                .setPositiveButton("Yes") { _, it ->
                    viewModel.deleteWord(navArgs.id)
                    setFragmentResult("from_details", bundle)
                    NavHostFragment.findNavController(this).popBackStack()
                }.setNegativeButton("No") { _, it -> }
                .show()
        }

        binding.btnCancel.setOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }

    }
}