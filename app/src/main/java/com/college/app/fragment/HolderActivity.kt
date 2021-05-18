package com.college.app.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.college.app.R
import com.college.app.databinding.ActivityHolderBinding

class HolderActivity : AppCompatActivity() {
    private var activityHolderBinding: ActivityHolderBinding? = null
    private fun openFragment(
        fragment: Class<out Fragment>,
        provider: String,
        data: Array<String>?
    ) {
        try {
            val frag = fragment.newInstance()

            // adding the data for the fragment , not required for all cases
            val bundle = Bundle()
            bundle.putStringArray(FRAGMENT_DATA, data)
            bundle.putString(FRAGMENT_PROVIDER, provider)
            frag.arguments = bundle

            //Changing the fragment
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.fragmentHolder, frag)
                .commit()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHolderBinding = ActivityHolderBinding.inflate(
            layoutInflater
        )
        setContentView(activityHolderBinding!!.root)
        setSupportActionBar(activityHolderBinding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val fragmentClass = intent.extras!!
            .getSerializable(FRAGMENT_CLASS) as Class<out Fragment>?
        val args = intent.extras!!.getStringArray(FRAGMENT_DATA)
        val provider = intent.extras!!
            .getString(FRAGMENT_PROVIDER, "")
        fragmentClass?.let { openFragment(it, provider, args) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        var FRAGMENT_DATA = "transaction_data"
        var FRAGMENT_CLASS = "transation_target"
        var FRAGMENT_PROVIDER = "transation_provider"
        fun startActivity(
            mContext: Context,
            fragment: Class<out Fragment?>?,
            provider: String?,
            data: Array<String>?
        ) {
            val bundle = Bundle()
            bundle.putStringArray(FRAGMENT_DATA, data)
            bundle.putSerializable(FRAGMENT_CLASS, fragment)
            bundle.putString(FRAGMENT_PROVIDER, provider)
            val intent = Intent(mContext, HolderActivity::class.java)
            intent.putExtras(bundle)
            mContext.startActivity(intent)
        }
    }
}