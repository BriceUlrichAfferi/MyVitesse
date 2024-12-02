package com.example.vitesse.adapters

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
    private val onFavoriteClick: (Candidate) -> Unit,
    private val onItemClick: (Candidate) -> Unit
) : RecyclerView.Adapter<CandidatAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val candidat = candidatList[position]

        Glide.with(holder.itemView.context)
            .load(candidat.picture.takeIf { it.isNotEmpty() } ?: R.drawable.personicon)
            .placeholder(R.drawable.personicon)
            .error(R.drawable.personicon)
            .into(holder.binding.imageCandidat)

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


    //Calculate between Old and New
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
        val diffCallback = CandidatDiffCallback(candidatList, newCandidats)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        candidatList = newCandidats
        diffResult.dispatchUpdatesTo(this)
    }
        class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)
}