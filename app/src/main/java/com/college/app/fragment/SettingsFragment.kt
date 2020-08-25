package com.college.app.fragment

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.college.app.R
import com.college.app.databinding.SettingsFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsFragment : Fragment() {
    private var settingsFragmentBinding: SettingsFragmentBinding? = null
    var button: RadioButton? = null
    private var selected: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        settingsFragmentBinding = SettingsFragmentBinding.inflate(inflater)
        val h = activity as HolderActivity?
        if (h != null) {
            h.setSupportActionBar(settingsFragmentBinding!!.toolbar)
            h.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            h.supportActionBar!!.title = "Settings"
            setHasOptionsMenu(true)
        }
        cardViews()
        return settingsFragmentBinding!!.root
    }

    private fun cardViews() {
        settingsFragmentBinding!!.darkCard.setOnClickListener { darkModeSwitch() }
        settingsFragmentBinding!!.appGithub.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(REPO_LINK))
            startActivity(browserIntent)
        }
        settingsFragmentBinding!!.feedbackForm.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(FEEDBACK_LINK))
            startActivity(browserIntent)
        }
    }

    private fun darkModeSwitch() {
        val sharedPreferences = activity!!.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        val builder = context?.let { MaterialAlertDialogBuilder(it) }
        val dialogView = layoutInflater.inflate(R.layout.dark_mode_dailog, null)
        builder?.setView(dialogView)
        val cancel = dialogView.findViewById<TextView>(R.id.cancelText)
        val okay = dialogView.findViewById<TextView>(R.id.okayText)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioDarkGroup)
        val sharedTheme = sharedPreferences.getString(THEME, "System Default")
        Log.d(ContentValues.TAG, "darkModeSwitch: shared value $sharedTheme")
        when (sharedTheme) {
            "System Default" -> radioGroup.check(R.id.radioButtonSystemDefault)
            "Dark Theme" -> radioGroup.check(R.id.radioButtonDark)
            "Light Theme" -> radioGroup.check(R.id.radioButtonLight)
        }
        val alertDialog = builder?.create()
        alertDialog?.show()
        cancel.setOnClickListener { alertDialog?.cancel() }
        val editor = sharedPreferences.edit()
        okay.setOnClickListener {
            button = dialogView.findViewById<View>(radioGroup.checkedRadioButtonId) as RadioButton
            selected = button!!.text.toString()
            editor.putString(THEME, selected)
            editor.apply()
            Log.d(ContentValues.TAG, "onClick: Okay mode: $selected")
            // set mode accordingly here
            toggleMode()
            alertDialog?.cancel()
        }
    }

    private fun toggleMode() {
        when (selected) {
            "System Default" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            "Dark Theme" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "Light Theme" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    companion object {
        const val THEME = "Theme"
        const val MyPREFERENCES = "MyPrefs"
        const val REPO_LINK = "http://www.github.com/4shutosh/CollegeAppIIITN"
        const val FEEDBACK_LINK = "https://forms.gle/CCCwsUunfb2nktPd8"
    }
}