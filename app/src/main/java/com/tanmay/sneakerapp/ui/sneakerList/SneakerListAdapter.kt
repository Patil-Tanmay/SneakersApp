package com.tanmay.sneakerapp.ui.sneakerList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tanmay.sneakerapp.data.SneakerItem
import com.tanmay.sneakerapp.databinding.ItemSneakerBinding

class SneakerListAdapter(private val onItemClick: (SneakerItem) -> Unit) :
    ListAdapter<SneakerItem, SneakerListAdapter.SneakerListViewHolder>(myItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SneakerListViewHolder {
        return SneakerListViewHolder(
            ItemSneakerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SneakerListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class SneakerListViewHolder(private val binding: ItemSneakerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sneakerItem: SneakerItem) {
            binding.sneakerNameText.isSelected = true
            Glide.with(binding.root)
                .load(sneakerItem.media.imageUrl)
                .into(binding.sneakerImgView)

            binding.sneakerNameText.text = sneakerItem.name
            binding.sneakerPrice.text = "$${sneakerItem.retailPrice}"

            binding.root.setOnClickListener {
                onItemClick(sneakerItem)
            }

        }
    }
}

// DiffCallback for efficient updates
val myItemDiffCallback = object : DiffUtil.ItemCallback<SneakerItem>() {
    override fun areItemsTheSame(oldItem: SneakerItem, newItem: SneakerItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SneakerItem, newItem: SneakerItem): Boolean {
        return oldItem == newItem
    }
}