package com.college.app.utils.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

inline fun <T: ViewDataBinding> Fragment.bindWithLifecycleOwner(
    inflater: LayoutInflater,
    @LayoutRes layoutId: Int,
    container: ViewGroup?,
    bind: (T.() -> Unit) = {}
): T {
    val binding: T = DataBindingUtil.inflate(inflater, layoutId, container, false)
    binding.lifecycleOwner = viewLifecycleOwner
    binding.invalidateAll()
    binding.bind()
    return binding
}