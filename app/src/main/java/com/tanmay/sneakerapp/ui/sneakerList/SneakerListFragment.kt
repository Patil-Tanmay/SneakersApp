package com.tanmay.sneakerapp.ui.sneakerList

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.tanmay.sneakerapp.R
import com.tanmay.sneakerapp.databinding.FragmentSneakerListBinding
import com.tanmay.sneakerapp.ui.sneakerDetails.SneakerDetailsFragment
import com.tanmay.sneakerapp.utils.Resource
import com.tanmay.sneakerapp.utils.ValueFragment
import com.tanmay.sneakerapp.utils.collectItems
import com.tanmay.sneakerapp.utils.hide
import com.tanmay.sneakerapp.utils.hideKeyboard
import com.tanmay.sneakerapp.utils.show
import com.tanmay.sneakerapp.utils.showKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SneakerListFragment :
    ValueFragment<FragmentSneakerListBinding>(R.layout.fragment_sneaker_list) {

    override fun attachBinding(view: View): FragmentSneakerListBinding =
        FragmentSneakerListBinding.bind(view)

    private val viewModel by viewModels<SneakerListViewModel>()


    private val sneakerListAdapter by lazy {
        SneakerListAdapter() {sneaker ->
            parentFragmentManager.commit {
                val sneakerDetailsFragment = SneakerDetailsFragment()
                sneakerDetailsFragment.arguments = Bundle().apply {
                    putParcelable("SneakerDetail",sneaker)
                }
                replace(R.id.fragContainer,sneakerDetailsFragment, SneakerDetailsFragment.TAG)
                addToBackStack(SneakerDetailsFragment.TAG)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpObservers()
        viewModel.getSneakersList(null)
    }

    private fun initView() {
        binding.apply {
            hideSearchBar(true)
            recView.layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            recView.hasFixedSize()
            recView.adapter = sneakerListAdapter

            searchBar.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                    // Perform your operation here
                    if (searchBar.text.toString().isEmpty() || searchBar.text.toString().isBlank()){
                        viewModel.getSneakersList(null)
                    }else {
                        viewModel.getSneakersList(searchBar.text.toString().trim())
                    }
                    hideKeyboard()
                    true
                }
                false
            }

            searchButton.setOnClickListener {
                hideSearchBar(false)
                searchBar.requestFocus()
                showKeyboard()
            }

            btnRemvText.setOnClickListener {
                binding.searchBar.setText("")
                hideKeyboard()
                hideSearchBar(true)
                viewModel.getSneakersList(null)
            }
        }
    }

    private fun hideSearchBar(hideSearchBar: Boolean){
        if (hideSearchBar){
            binding.searchBar.hide()
            binding.btnRemvText.hide()
            binding.titleText.show()
            binding.searchButton.show()
        }else{
            // showing the search bar
            binding.searchBar.show()
            binding.btnRemvText.show()
            binding.titleText.hide()
            binding.searchButton.hide()
        }
    }


    private fun setUpObservers() {

        viewModel.sneakerList.collectItems(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.recView.show()
                    binding.progressLay.hide()
                    binding.errorText.hide()
                    sneakerListAdapter.submitList(it.data)
                }
                Resource.Loading -> {
                    binding.recView.hide()
                    binding.progressLay.show()
                    binding.errorText.hide()
                }
                is Resource.Error -> {
                    binding.errorText.text = it.message
                    binding.recView.hide()
                    binding.progressLay.hide()
                    binding.errorText.show()
                }
            }

        }
    }
}