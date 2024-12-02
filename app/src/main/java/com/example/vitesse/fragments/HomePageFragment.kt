package com.example.vitesse.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.example.vitesse.R
import com.example.vitesse.adapters.ViewPagerAdapter
import com.example.vitesse.databinding.FragmentHomePageBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageFragment : Fragment() {

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        val view = binding.root

        // Setup the SearchView and customize it
        customizeSearchView()

        binding.ajout.setOnClickListener {
            // Navigate to AddFragment
            findNavController().navigate(R.id.addFragment)
        }

        // Setup ViewPager with FragmentStateAdapter
        setupViewPager()

        // Setup search functionality
        setupSearchView()

        return view
    }

    // Customize the SearchView
    private fun customizeSearchView() {
        val searchView = binding.searchBar

        // Get the SearchView's LinearLayout
        val linearLayout = searchView.findViewById<LinearLayout>(androidx.appcompat.R.id.search_plate)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ).apply {
            marginEnd = 16
        }

        // Image of the SearchView's LinearLayout
        val imageView = ImageView(requireContext()).apply {
            setImageResource(R.drawable.baseline_search_24)
            contentDescription = getString(R.string.filtrer)
            layoutParams.gravity = Gravity.CENTER_VERTICAL
        }
        linearLayout.addView(imageView, layoutParams)
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(
            requireActivity(),
            onItemClickListener = {candidat ->
                findNavController().navigate(R.id.editFragment)
            }
        )
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.all)
                1 -> getString(R.string.favoris)
                else -> null
            }
        }.attach()

    }

    private fun setupSearchView() {
        binding.searchBar.setQueryHint(getString(R.string.search_a_candidate))
        binding.searchBar.setIconifiedByDefault(false)

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Access CandidatsFragment directly from the adapter
                val fragment = (binding.viewPager.adapter as ViewPagerAdapter).getCandidatsFragment()
                fragment.filterCandidates(newText ?: "")
                return true
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
