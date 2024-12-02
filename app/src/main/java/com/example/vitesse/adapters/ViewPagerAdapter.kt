package com.example.vitesse.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.vitesse.fragments.CandidatsFragment
import com.example.vitesse.fragments.FavorisFragment
import com.example.vitesse.model.Candidate
import com.example.vitesse.utils.FilterableCandidates

class ViewPagerAdapter(activity: FragmentActivity, private val onItemClickListener: (Candidate) -> Unit) : FragmentStateAdapter(activity) {

    private val candidatsFragment = CandidatsFragment()
    private val favorisFragment = FavorisFragment()

     override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                candidatsFragment.setOnItemClickListener(onItemClickListener)
                candidatsFragment
            }
            1 -> favorisFragment
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }

    fun getCandidatsFragment(): FilterableCandidates {
        return candidatsFragment
    }
}
