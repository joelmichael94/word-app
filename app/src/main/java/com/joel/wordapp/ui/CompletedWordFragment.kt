package com.joel.wordapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joel.wordapp.R
import com.joel.wordapp.databinding.FragmentCompletedWordBinding

class CompletedWordFragment private constructor() : Fragment() {
    private lateinit var binding: FragmentCompletedWordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompletedWordBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
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