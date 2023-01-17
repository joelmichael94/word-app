package com.joel.wordapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

// ViewPagerAdapter class, contains functions and information for the different Tab Layout fragments (NewWord and CompletedWord) -> it is called in MainFragment.kt
class ViewPagerAdapter(
    val fragments: List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    // function that returns the number of fragments in the list
    override fun getItemCount() = fragments.size

    // function that creates the fragment UI depending on the position selected in the TabLayout
    override fun createFragment(position: Int) = fragments[position]
}