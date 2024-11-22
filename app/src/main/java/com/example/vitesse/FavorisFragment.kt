package com.example.vitesse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vitesse.adapters.FavorisAdapter
import com.example.vitesse.databinding.FragmentFavorisBinding
import com.example.vitesse.model.Candidate
import kotlinx.coroutines.launch

class FavorisFragment : Fragment() {

    private var onItemClickListener: ((Candidate) -> Unit)? = null
    private var _binding: FragmentFavorisBinding? = null
    private val binding get() = _binding!!

    private lateinit var favorisAdapter: FavorisAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavorisBinding.inflate(inflater, container, false)
        return binding.root
    }
    // Add a method to set the onItemClickListener
    fun setOnItemClickListener(listener: (Candidate) -> Unit) {
        onItemClickListener = listener
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // Observe the favorites list and update the adapter
        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.favorites.collect { favorites ->
                if (favorites.isEmpty()) {
                    binding.emptyView.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.emptyView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    favorisAdapter.updateFavorites(favorites) // Update the adapter with the new favorites list
                }
            }
        }

    }

    private fun setupRecyclerView() {
        favorisAdapter = FavorisAdapter(
            emptyList(),// Initially, the list is empty
            onItemClick = { candidate ->

                Log.d("CandidatsFragment", "Selected candidate image URL: ${candidate.picture}")

                // Set the selected candidate in SharedViewModel
                sharedViewModel.setSelectedCandidat(candidate)

                // Navigate to DetailsFragment when an item is clicked
                findNavController().navigate(R.id.detailsFragment) // Navigate back

            })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favorisAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
