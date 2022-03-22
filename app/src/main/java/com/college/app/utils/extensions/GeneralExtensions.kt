package com.college.app.utils.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.toLiveData(): LiveData<T> = this


inline fun <T : ViewDataBinding> T.executeAfter(block: T.() -> Unit) {
    block()
    executePendingBindings()
}

inline fun <T : ViewDataBinding> bindWithLayout(
    @LayoutRes layoutId: Int, parent: ViewGroup, bind: (T.() -> Unit) = {},
): T {
    val inflater: LayoutInflater = LayoutInflater.from(parent.context)
    val binding: T = DataBindingUtil.inflate(inflater, layoutId, parent, false)
    binding.bind()
    return binding
}

fun <E> List<E>?.sublistOrEmpty(endIndex: Int): List<E> {
    return this?.let { if (size > endIndex) it.subList(0, endIndex) else this } ?: emptyList()
}


fun Fragment.showToast(message: String){
    requireContext().showToast(message)
}


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Long?.orDef() = 0L
fun Int?.orDef() = 0L