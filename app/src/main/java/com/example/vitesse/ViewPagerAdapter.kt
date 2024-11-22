package com.example.vitesse

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.vitesse.candidats.CandidatsFragment
import com.example.vitesse.FavorisFragment
import com.example.vitesse.model.Candidate
import com.example.vitesse.utils.FilterableCandidates

class ViewPagerAdapter(activity: FragmentActivity, private val onItemClickListener: (Candidate) -> Unit) : FragmentStateAdapter(activity) {

    private val candidatsFragment = CandidatsFragment() // Store an instance of CandidatsFragment
    private val favorisFragment = FavorisFragment() // Store an instance of FavorisFragment

    companion object {
        fun getFragmentTag(viewPagerId: Int, position: Int): String {
            return "f${viewPagerId}_$position"
        }
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                candidatsFragment.setOnItemClickListener(onItemClickListener) // Pass the listener to CandidatsFragment
                candidatsFragment
            }
            1 -> favorisFragment
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }

    // New method to get the CandidatsFragment instance
    fun getCandidatsFragment(): FilterableCandidates {
        return candidatsFragment as FilterableCandidates
    }
}
