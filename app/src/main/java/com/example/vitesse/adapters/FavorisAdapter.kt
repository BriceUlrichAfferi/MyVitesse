package com.example.vitesse.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vitesse.R
import com.example.vitesse.databinding.ListItemBinding
import com.example.vitesse.model.Candidate

class FavorisAdapter(
    private var favoritesList: List<Candidate>,
    private val onItemClick: (Candidate) -> Unit
) : RecyclerView.Adapter<FavorisAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val candidat = favoritesList[position]

        // Use Glide to load the candidate image
        Glide.with(holder.itemView.context)
            .load(candidat.picture)
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
        return favoritesList.size
    }

    // Update the list of favorites in the adapter
    fun updateFavorites(newFavorites: List<Candidate>) {
        favoritesList = newFavorites
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)
}
