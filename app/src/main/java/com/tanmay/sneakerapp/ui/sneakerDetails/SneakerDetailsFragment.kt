package com.tanmay.sneakerapp.ui.sneakerDetails

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.tanmay.sneakerapp.R
import com.tanmay.sneakerapp.data.SneakerItem
import com.tanmay.sneakerapp.databinding.FragmentSneakerDetailsBinding
import com.tanmay.sneakerapp.utils.Resource
import com.tanmay.sneakerapp.utils.ValueFragment
import com.tanmay.sneakerapp.utils.collectItems
import com.tanmay.sneakerapp.utils.hide
import com.tanmay.sneakerapp.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SneakerDetailsFragment :
    ValueFragment<FragmentSneakerDetailsBinding>(R.layout.fragment_sneaker_details) {

    override fun attachBinding(view: View): FragmentSneakerDetailsBinding =
        FragmentSneakerDetailsBinding.bind(view)

    private val viewModel by viewModels<SneakerDetailsViewModel>()

    private val sneakerItem by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // code for TIRAMISU and higher
            arguments?.getParcelable("SneakerDetail", SneakerItem::class.java)
        } else {
            // Alternative implementation for lower Android versions
            arguments?.getParcelable<SneakerItem>("SneakerDetail")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBackPressed {
            parentFragmentManager.popBackStack()
        }

        initView()
        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.addOrRemoveSneakerToCart.collectItems(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.addToCartProgressBar.hide()
                    enableDisableAddCart(it.data.isAddedToCart)
                }

                Resource.Loading -> {
                    binding.addToCartProgressBar.show()
                    binding.btnRemoveCart.hide()
                    binding.btnAddToCart.hide()
                }

                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Error! Please try again ;)",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.addToCartProgressBar.hide()
                    enableDisableAddCart(sneakerItem?.isAddedToCart)
                }
            }

        }
    }

    private fun enableDisableAddCart(isAddToCart: Boolean?) {
        if (isAddToCart == true) {
            binding.btnAddToCart.hide()
            binding.btnRemoveCart.show()
        } else {
            binding.btnAddToCart.show()
            binding.btnRemoveCart.hide()
        }
    }


    private fun initView() {

        binding.apply {
            addToCartProgressBar.hide()
            sneakerItem?.let { sneaker ->
                enableDisableAddCart(sneaker.isAddedToCart)

                Glide.with(binding.root)
                    .load(sneaker.media.imageUrl)
                    .into(mainImgView)

                sneakerNameText.text = sneaker.name
                sneakerReleaseInfo.text = "Year of Release : ${sneaker.releaseDate}"
                sneakerBrand.text = "Brand : ${sneaker.brand}"
                sneakerPrice.text = "Price: $${sneaker.retailPrice}"

                btnBack.setOnClickListener { parentFragmentManager.popBackStack() }
            }
        }


        binding.btnAddToCart.setOnClickListener {
            if (sneakerItem != null) {
                viewModel.addOrRemoveSneakerFromCart(sneakerItem!!, true)
            }
        }

        binding.btnRemoveCart.setOnClickListener {

            if (sneakerItem != null) {
                viewModel.addOrRemoveSneakerFromCart(sneakerItem!!, false)
            }

        }
    }

    companion object {
        const val TAG = "SneakerDetailsFragment"
    }
}