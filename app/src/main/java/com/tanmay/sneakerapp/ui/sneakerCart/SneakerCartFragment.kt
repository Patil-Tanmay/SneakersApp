package com.tanmay.sneakerapp.ui.sneakerCart

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tanmay.sneakerapp.R
import com.tanmay.sneakerapp.databinding.FragmentSneakerCartBinding
import com.tanmay.sneakerapp.utils.Resource
import com.tanmay.sneakerapp.utils.ValueFragment
import com.tanmay.sneakerapp.utils.collectItems
import com.tanmay.sneakerapp.utils.hide
import com.tanmay.sneakerapp.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SneakerCartFragment : ValueFragment<FragmentSneakerCartBinding>(R.layout.fragment_sneaker_cart) {

    override fun attachBinding(view: View): FragmentSneakerCartBinding = FragmentSneakerCartBinding.bind(view)

    private val viewModel by viewModels<SneakerCartViewModel>()

    private val cartSneakerAdapter by lazy{
        CartAddedSneakerAdapter(){sneakerItem ->
            viewModel.removeSneakerFromCart(sneakerItem)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpObservers()
        viewModel.getSneakersFromCart()
    }

    private fun initView() {
        binding.apply {
            recView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recView.hasFixedSize()
            recView.adapter = cartSneakerAdapter

            btnCheckOut.setOnClickListener {
                Toast.makeText(requireContext(), "Processing Payment :dummy payment message", Toast.LENGTH_SHORT)
            }
        }
    }


    private fun setUpObservers() {

        viewModel.cartSneakerList.collectItems(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressLay.hide()
                    if(it.data.isNullOrEmpty()){
                        binding.mainLayout.hide()
                        binding.errorText.show()
                        binding.errorText.text = "No Sneakers Added to the cart."
                    }else {
                        binding.errorText.hide()
                        binding.mainLayout.show()
                        cartSneakerAdapter.submitList(it.data)
                        calculateTotal(it.data.map { item -> item.retailPrice })
                    }
                }
                Resource.Loading -> {
                    binding.mainLayout.hide()
                    binding.progressLay.show()
                    binding.errorText.hide()
                }
                is Resource.Error -> {
                    binding.errorText.text = it.message ?: "Unable to fetch Cart Details."
                    binding.mainLayout.hide()
                    binding.progressLay.hide()
                    binding.errorText.show()
                }
            }

        }
    }

    private fun calculateTotal(allSneakerCostList : List<Int>){
        val totalPrice = allSneakerCostList.sum()
        // assuming a random number for taxes
        binding.priceCalculationText.text = "Subtotal: $${totalPrice}\nTaxes and Charges: $40"

        binding.totalAmoountText.text = "Total: $${totalPrice +  40}"

    }


    companion object {

    }
}