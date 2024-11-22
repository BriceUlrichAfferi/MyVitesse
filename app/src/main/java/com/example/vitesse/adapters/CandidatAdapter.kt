package com.example.vitesse.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vitesse.R
import com.example.vitesse.databinding.ListItemBinding
import com.example.vitesse.model.Candidate

class CandidatAdapter(
    private var candidatList: List<Candidate>,
    private val onFavoriteClick: (Candidate) -> Unit, // Callback for favorite action
    private val onItemClick: (Candidate) -> Unit // Callback for item click
) : RecyclerView.Adapter<CandidatAdapter.ViewHolder>() {

    // Set containing the favorite candidates

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val candidat = candidatList[position]

        // Use Glide to load the image from the URL into the ImageView
        Glide.with(holder.itemView.context)
            .load(candidat.picture.takeIf { it.isNotEmpty() } ?: R.drawable.personicon) // Load image from URL
            .placeholder(R.drawable.personicon) // Optional: set a placeholder while loading
            .error(R.drawable.personicon) // Optional: set an error image if URL loading fails
            .into(holder.binding.imageCandidat) // Target ImageView



        holder.binding.firstName.text = candidat.firstName
        holder.binding.lastName.text = candidat.lastName
        holder.binding.note.text = candidat.note


        // Handle item click for navigating to details
        holder.itemView.setOnClickListener {
            onItemClick(candidat)
        }
    }

    override fun getItemCount(): Int {
        return candidatList.size
    }

    class CandidatDiffCallback(
        private val oldList: List<Candidate>,
        private val newList: List<Candidate>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }

    fun updateCandidats(newCandidats: List<Candidate>) {
        Log.d("CandidatAdapter", "Old list size: ${candidatList.size}, New list size: ${newCandidats.size}")
        val diffCallback = CandidatDiffCallback(candidatList, newCandidats)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        candidatList = newCandidats
        diffResult.dispatchUpdatesTo(this)
    }






    class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)
}
