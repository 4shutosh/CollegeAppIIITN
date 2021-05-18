package com.college.app.adapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.lang.ref.WeakReference
import java.util.*

class ViewPagerAdapter : FragmentPagerAdapter {
    private val instantiatedFragments = SparseArray<WeakReference<Fragment>>()
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private var mFragmentTitleList: List<String> = ArrayList()

    constructor(supportFragmentManager: FragmentManager?, mFragmentTitleList: List<String>) : super(
        supportFragmentManager!!
    ) {
        this.mFragmentTitleList = mFragmentTitleList
    }

    constructor(supportFragmentManager: FragmentManager?) : super(
        supportFragmentManager!!
    ) {
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        instantiatedFragments.put(position, WeakReference(fragment))
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        instantiatedFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getFragment(position: Int): Fragment? {
        val wr = instantiatedFragments[position]
        return wr?.get()
    }
}