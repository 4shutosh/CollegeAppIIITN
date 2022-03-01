package com.college.app.utils.extensions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

inline fun <T : ViewDataBinding> Fragment.bindWithLifecycleOwner(
    inflater: LayoutInflater,
    @LayoutRes layoutId: Int,
    container: ViewGroup?,
    bind: (T.() -> Unit) = {},
): T {
    val binding: T = DataBindingUtil.inflate(inflater, layoutId, container, false)
    binding.lifecycleOwner = viewLifecycleOwner
    binding.bind()
    return binding
}

/**
 * The function first checks if the bottomsheet has already been shown
 * using this FragmentManager,
 * If it has not, it creates an instance of the bottomsheet and displays
 *
 * @param bundle Optional bundle to pass ar argument to the bottomsheet
 */
@Suppress("UNCHECKED_CAST")
fun <T> FragmentManager.showBottomSheetIsNotPresent(
    bottomSheetDialogFragmentClass: Class<*>,
    tag: String,
    bundle: Bundle? = null,
): T {
    /**
     * Check if bottomSheetDialogFragmentClass extends BottomSheetDialogFragment
     */
    if (!(BottomSheetDialogFragment::class.java.isAssignableFrom(bottomSheetDialogFragmentClass))) {
        throw Exception("bottomSheetDialogFragmentClass does not extend com.google.android.material.bottomsheet.BottomSheetDialogFragment")
    }

    /**
     * Check if the FragmentManager already contains a fragment with the given tag
     */
    return if (this.findFragmentByTag(tag) == null) {
        val bottomSheetDialogFragment =
            bottomSheetDialogFragmentClass.newInstance() as BottomSheetDialogFragment
        bottomSheetDialogFragment.arguments = bundle
        if (!this.isStateSaved) {
            bottomSheetDialogFragment.show(this, tag)
        }
        bottomSheetDialogFragment as T
    } else {
        this.findFragmentByTag(tag) as BottomSheetDialogFragment as T
    }
}

fun FragmentManager.showBottomSheetIsNotPresent(
    bottomSheetDialogFragmentClass: DialogFragment,
    tag: String,
): DialogFragment {

    return if (this.findFragmentByTag(tag) == null && !this.isStateSaved) {
        bottomSheetDialogFragmentClass.show(this, tag)
        bottomSheetDialogFragmentClass
    } else {
        this.findFragmentByTag(tag) as DialogFragment
    }
}

/**
 * The function first checks if the DialogFragment has already been shown
 * using this FragmentManager,
 * If it has not, it creates an instance of the DialogFragment and displays
 *
 * @param bundle Optional bundle to pass ar argument to the DialogFragment
 */
@Suppress("UNCHECKED_CAST")
fun <T> FragmentManager.showDialogFragmentIfNotPresent(
    dialogFragmentClass: Class<*>,
    tag: String,
    bundle: Bundle? = null,
): T {
    /**
     * Check if bottomSheetDialogFragmentClass extends BottomSheetDialogFragment
     */
    if (!(DialogFragment::class.java.isAssignableFrom(dialogFragmentClass))) {
        throw Exception("dialogFragmentClass does not extend androidx.fragment.app.DialogFragment")
    }

    /**
     * Check if the FragmentManager already contains a fragment with the given tag
     */
    return if (this.findFragmentByTag(tag) == null && !this.isStateSaved) {
        val dialogFragment = dialogFragmentClass.newInstance() as DialogFragment
        dialogFragment.arguments = bundle
        dialogFragment.show(this, tag)
        dialogFragment as T
    } else {
        this.findFragmentByTag(tag) as DialogFragment as T
    }
}