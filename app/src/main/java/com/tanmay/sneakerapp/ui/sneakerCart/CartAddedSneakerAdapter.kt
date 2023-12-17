package com.tanmay.sneakerapp.ui.sneakerCart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tanmay.sneakerapp.data.SneakerItem
import com.tanmay.sneakerapp.databinding.ItemCartSneakerBinding
import com.tanmay.sneakerapp.ui.sneakerList.myItemDiffCallback

class CartAddedSneakerAdapter(private val onItemClick: (SneakerItem) -> Unit) :
    ListAdapter<SneakerItem, CartAddedSneakerAdapter.CartSneakersViewHolder>(myItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartSneakersViewHolder {
        return CartSneakersViewHolder(
            ItemCartSneakerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartSneakersViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class CartSneakersViewHolder(private val binding: ItemCartSneakerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sneakerItem: SneakerItem) {
            Glide.with(binding.root)
                .load(sneakerItem.media.imageUrl)
                .into(binding.sneakerImgView)

            binding.sneakerNameText.text = sneakerItem.name
            binding.sneakerPrice.text = "$${sneakerItem.retailPrice}"

            binding.icRemoveSneaker.setOnClickListener{
                onItemClick(sneakerItem)
            }
        }
    }
}
