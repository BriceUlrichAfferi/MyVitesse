package com.example.vitesse

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.vitesse.candidats.CandidatsFragment
import com.example.vitesse.databinding.ActivityHomePageBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageFragment : Fragment() {

    private var _binding: ActivityHomePageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = ActivityHomePageBinding.inflate(inflater, container, false)
        val view = binding.root

        // Setup the SearchView and customize it
        customizeSearchView()

        binding.ajout.setOnClickListener {
            // Navigate to AddFragment using NavController
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
            LinearLayout.LayoutParams.MATCH_PARENT // Match parent height to center vertically
        ).apply {
            marginEnd = 16 // Add margin if needed
        }

        // Create a new ImageView and add it to the SearchView's LinearLayout
        val imageView = ImageView(requireContext()).apply {
            setImageResource(R.drawable.baseline_search_24) // Your icon here
            contentDescription = getString(R.string.filtrer)
            layoutParams.gravity = Gravity.CENTER_VERTICAL // Center vertically
        }

        // Add ImageView at the end of the LinearLayout
        linearLayout.addView(imageView, layoutParams)
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(
            requireActivity(),
            onItemClickListener = { candidat ->
                // Handle item click (you can navigate to a details screen or do other actions)
                findNavController().navigate(R.id.editFragment) // Navigate back
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

        // Listen for page selection to ensure fragments are attached
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    Log.d("HomePageFragment", "CandidatsFragment should be attached now")
                }
            }
        })
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

                Log.d("HomePageFragment", "Filtering candidates with query: $newText")
                fragment.filterCandidates(newText ?: "")
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding reference
    }
}
