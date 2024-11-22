package com.example.vitesse
/*
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.vitesse.candidats.CandidatsFragment
import com.example.vitesse.databinding.ActivityHomePageBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
// Setup the SearchView and customize it
        customizeSearchView()


        binding.ajout.setOnClickListener {

        }

        // Set up ViewPager with FragmentStateAdapter
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        // Attach TabLayout with ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Tous"
                1 -> "Favoris"
                else -> null
            }
        }.attach()
        // Setup search functionality
        setupSearchView()
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
        val imageView = ImageView(this).apply {
            setImageResource(R.drawable.baseline_search_24) // Your icon here
            contentDescription = "Filter"
            layoutParams.gravity = Gravity.CENTER_VERTICAL // Center vertically
        }

        // Add ImageView at the end of the LinearLayout
        linearLayout.addView(imageView, layoutParams)
    }



    private fun setupSearchView() {
        binding.searchBar.setQueryHint("Rechercher un candidat")
        binding.searchBar.setIconifiedByDefault(false)

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val fragment = supportFragmentManager.findFragmentByTag("f0") as? CandidatsFragment
                if (fragment != null) {
                    Log.d("HomePageActivity", "Filtering candidates with query: $newText")
                    fragment.filterCandidates(newText ?: "")
                } else {
                    Log.e("HomePageActivity", "CandidatsFragment not found")
                }
                return true
            }
        })
    }
}*/