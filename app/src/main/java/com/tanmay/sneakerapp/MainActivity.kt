package com.tanmay.sneakerapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.tanmay.sneakerapp.databinding.ActivityMainBinding
import com.tanmay.sneakerapp.ui.sneakerCart.SneakerCartFragment
import com.tanmay.sneakerapp.ui.sneakerDetails.SneakerDetailsFragment
import com.tanmay.sneakerapp.ui.sneakerList.SneakerListFragment
import com.tanmay.sneakerapp.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private lateinit var sneakerCartFragment: SneakerCartFragment
    private lateinit var sneakerListFragment: SneakerListFragment

    private val fragments: Array<Fragment> get() = arrayOf(sneakerListFragment, sneakerCartFragment)
    private var selectedIndex = 0

    private val tabs: Array<ImageView>
        get() = binding.run {
            arrayOf(listFragText, cartFragText)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val listFragment = SneakerListFragment().also { this.sneakerListFragment = it }
            val cartFragment = SneakerCartFragment().also { this.sneakerCartFragment = it }

            supportFragmentManager.beginTransaction()
                .add(R.id.fragContainer, listFragment, "listFragment")
                .add(R.id.fragContainer, cartFragment, "cartFragment")
                .selectFragment(selectedIndex)
                .commit()
        } else {
            selectedIndex = savedInstanceState.getInt("selectedIndex", 0)

            sneakerListFragment =
                supportFragmentManager.findFragmentByTag("listFragment") as SneakerListFragment
            sneakerCartFragment =
                supportFragmentManager.findFragmentByTag("cartFragment") as SneakerCartFragment

        }

        tabs.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                selectFragment(index)
            }
        }

        setupTabSelectedState(selectedIndex)

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (selectedIndex != 0) {
                    selectFragment(0)
                    setupTabSelectedState(0)
                } else {
                    finish()
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectedIndex", selectedIndex)
    }

    private fun FragmentTransaction.selectFragment(selectedIndex: Int): FragmentTransaction {
        fragments.forEachIndexed { index, fragment ->
            if (index == selectedIndex) {
                attach(fragment)
            } else {
                detach(fragment)
            }
        }

        return this
    }

    private fun setupTabSelectedState(selectedIndex: Int) {
        tabs.forEachIndexed { index, imgView ->

            when (index) {
                0 ->{
                    if(index == selectedIndex) {
                        imgView.setImageResource(R.drawable.ic_home_white)
                        imgView.setBackgroundResource(R.drawable.bg_circle_orange)
                    }else{
                        imgView.setImageResource(R.drawable.ic_home_orange)
                        imgView.setBackgroundResource(R.drawable.bg_circle_white)
                    }
                }

                1  ->{
                    if(index == selectedIndex) {
                        imgView.setImageResource(R.drawable.ic_shopping_cart_white)
                        imgView.setBackgroundResource(R.drawable.bg_circle_orange)
                    }else{
                        imgView.setImageResource(R.drawable.ic_shopping_cart_orange)
                        imgView.setBackgroundResource(R.drawable.bg_circle_white)
                    }
                }

                else -> {

                }

            }

        }
    }

    private fun selectFragment(indexToSelect: Int) {
        this.selectedIndex = indexToSelect

        setupTabSelectedState(indexToSelect)
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }
        supportFragmentManager.beginTransaction()
            .selectFragment(indexToSelect)
            .commit()
    }
}