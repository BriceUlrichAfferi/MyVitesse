package com.example.vitesse.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vitesse.R
import com.example.vitesse.data.viewModel.SharedViewModel
import com.example.vitesse.adapters.CandidatAdapter
import com.example.vitesse.data.viewModel.LocalCandidatViewModel
import com.example.vitesse.data.viewModel.RemoteCandidatViewModel
import com.example.vitesse.databinding.FragmentCandidatsBinding
import com.example.vitesse.model.Candidate
import com.example.vitesse.utils.FilterableCandidates
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CandidatsFragment : Fragment(), FilterableCandidates {

    private var _binding: FragmentCandidatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var candidatAdapter: CandidatAdapter
    private lateinit var loadingProgressBar: ProgressBar
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val localViewModel: LocalCandidatViewModel by viewModels()
    private val apiViewModel: RemoteCandidatViewModel by viewModels()
    private val combinedCandidats = mutableListOf<Candidate>()

    private var onItemClickListener: ((Candidate) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCandidatsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingProgressBar = binding.progressBar
        setupRecyclerView()
        apiViewModel.fetchCandidats()
        observeCandidates()
    }
    fun setOnItemClickListener(listener: (Candidate) -> Unit) {
        this.onItemClickListener = listener
    }
    private fun setupRecyclerView() {
        candidatAdapter = CandidatAdapter(
            emptyList(),
            onFavoriteClick = { candidat -> toggleFavorite(candidat) },
            onItemClick = { candidat ->
                onItemClickListener?.invoke(candidat)
                sharedViewModel.setSelectedCandidat(candidat)
                findNavController().navigate(R.id.detailsFragment)
            }
        )
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = candidatAdapter
        }
    }
    private fun observeCandidates() {
        viewLifecycleOwner.lifecycleScope.launch {
            localViewModel.candidateFlow.collect { localCandidates ->
                apiViewModel.remoteCandidats.observe(viewLifecycleOwner) { apiCandidats ->
                    val combinedCandidatesSet = HashSet<Candidate>()
                    combinedCandidatesSet.addAll(localCandidates)
                    apiCandidats.forEach { apiCandidate ->
                        if (!combinedCandidatesSet.any { it.id == apiCandidate.id }) {
                            combinedCandidatesSet.add(apiCandidate)
                        }
                    }

                    val combinedCandidatesList = combinedCandidatesSet.toList()
                    combinedCandidats.clear() // Clear old data
                    combinedCandidats.addAll(combinedCandidatesList) // Add new data

                    updateCandidateList(combinedCandidatesList, combinedCandidatesList.isNotEmpty())
                }
            }
        }
    }
    private fun updateCandidateList(candidates: List<Candidate>, shouldHideLoading: Boolean) {
        if (shouldHideLoading) {
            loadingProgressBar.visibility = View.GONE
        }

        if (candidates.isEmpty()) {
            binding.emptyText.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.emptyText.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            candidatAdapter.updateCandidats(candidates)
        }
    }

    override fun filterCandidates(query: String) {
        val filteredList = combinedCandidats.filter { candidat ->
            candidat.firstName?.contains(query, ignoreCase = true) == true ||
                    candidat.lastName?.contains(query, ignoreCase = true) == true
        }
        candidatAdapter.updateCandidats(filteredList)
        if (filteredList.isEmpty()) {
            binding.emptyQuery.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.emptyQuery.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    private fun toggleFavorite(candidate: Candidate) {
        sharedViewModel.toggleFavorite(candidate)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
