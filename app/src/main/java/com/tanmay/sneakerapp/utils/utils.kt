package com.tanmay.sneakerapp.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <reified T> Moshi.parseList(jsonString: String): List<T>? {
    return adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java)).fromJson(jsonString)
}

fun <T> Flow<T>.collectItems(
    owner: LifecycleOwner,
    emitData : ((T) -> Unit)?=null
) = owner.lifecycleScope.launch {
    flowWithLifecycle(owner.lifecycle, Lifecycle.State.STARTED).collect { emitData?.invoke(it) }
}


class OnBackPressedDelegationImpl : OnBackPressedDelegation, DefaultLifecycleObserver {

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackPressed.invoke()
        }
    }

    private var fragmentActivity: FragmentActivity? = null

    private lateinit var onBackPressed: () -> Unit

    override fun registerOnBackPressedDelegation(
        fragmentActivity: FragmentActivity?,
        lifecycle: Lifecycle,
        onBackPressed: () -> Unit
    ) {
        this.fragmentActivity = fragmentActivity
        this.onBackPressed = onBackPressed
        lifecycle.addObserver(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        fragmentActivity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        onBackPressedCallback.remove()
    }
}

interface OnBackPressedDelegation {

    fun registerOnBackPressedDelegation(
        fragmentActivity: FragmentActivity?,
        lifecycle: Lifecycle,
        onBackPressed: () -> Unit
    )
}

interface OnBackPressed {
    fun onBackPressed(block: () -> Unit)
}

abstract class ValueFragment<T : ViewBinding>(@LayoutRes fragxml: Int) : Fragment(fragxml),
    OnBackPressedDelegation by OnBackPressedDelegationImpl(), OnBackPressed {

    protected val binding by viewBinding(::attachBinding)
    abstract fun attachBinding(view: View): T
    override fun onBackPressed(block: () -> Unit) {
        registerOnBackPressedDelegation(requireActivity(), viewLifecycleOwner.lifecycle) {
            block()
        }
    }
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun Fragment.hideKeyboard() {
    view?.let { it.hideKeyboard() }
}

fun Fragment.showKeyboard() {
    view?.let { it.showKeyboard() }
}

private fun View.showKeyboard() { // Or View.showKeyboard()
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(
        InputMethod.SHOW_FORCED,
        InputMethodManager.HIDE_IMPLICIT_ONLY
    )
}

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}