package com.example.vitesse.candidats

/*import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vitesse.CandidatViewModel
import com.example.vitesse.adapters.CandidatAdapter
import com.example.vitesse.adapters.room.Candidat
import com.example.vitesse.data.viewModel.RemoteCandidatViewModel
import com.example.vitesse.databinding.ActivityCandidatsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CandidatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCandidatsBinding
    private lateinit var candidatAdapter: CandidatAdapter
    private lateinit var loadingProgressBar: ProgressBar

    // Use the viewModels delegate to obtain the ViewModel instance
    private val viewModel: RemoteCandidatViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCandidatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingProgressBar = binding.progressBar

        // Initialize the RecyclerView
        setupRecyclerView()

        // Load data
        loadCandidatsData()
    }

    private fun setupRecyclerView() {
        // Pass both the candidate list and the click listener
        candidatAdapter = CandidatAdapter(emptyList()) { candidat ->
            // Define what should happen when an item is clicked (e.g., toggle favorite)
            toggleFavorite(candidat)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@CandidatsActivity)
            adapter = candidatAdapter
        }
    }


    private fun loadCandidatsData() {
        // Show ProgressBar while loading
        loadingProgressBar.visibility = ProgressBar.VISIBLE

        viewModel.remoteCandidats // Call the method to load candidates

        // Observe the list of candidates
        viewModel.remoteCandidats.observe(this) { candidats ->
            // Hide ProgressBar
            loadingProgressBar.visibility = ProgressBar.GONE

            // Check if list is empty
            if (candidats.isEmpty()) {
                // Show the empty TextView and hide RecyclerView
                binding.emptyText.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                // Show the RecyclerView and hide the empty TextView
                binding.emptyText.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE

                // Update the adapter with the new list of candidates
                candidatAdapter.updateCandidats(candidats)
            }
        }
    }

    private fun toggleFavorite(candidat: Candidat) {
        // Implement the logic to handle favorite toggling
        // For example, add/remove the candidate from favorites
        // Or you can use a shared ViewModel to track favorite candidates
    }
    }*/