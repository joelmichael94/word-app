package com.joel.wordapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joel.wordapp.R
import com.joel.wordapp.databinding.FragmentNewWordBinding

class NewWordFragment private constructor() : Fragment() {
    private lateinit var binding: FragmentNewWordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewWordBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
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