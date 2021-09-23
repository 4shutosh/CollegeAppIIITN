package com.college.app.fragment

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.college.app.databinding.TimetableFragmentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TimeTableFragment : Fragment() {
    var timetableFragmentBinding: TimetableFragmentBinding? = null
    var link: String = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        timetableFragmentBinding = TimetableFragmentBinding.inflate(inflater)
        links
        return timetableFragmentBinding!!.root
    }

    private fun webView() {
//        WebSettings webSettings = timetableFragmentBinding.webViewTimeTable.getSettings();
        timetableFragmentBinding!!.webViewTimeTable.loadUrl(link)
        timetableFragmentBinding!!.webViewTimeTable.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                timetableFragmentBinding!!.progressBarTimeTable.visibility = View.GONE
                super.onPageFinished(view, url)
            }
        }
        timetableFragmentBinding!!.webViewTimeTable.settings.javaScriptEnabled = true
    }

    private val links: Unit
        get() {
            val eceReference = FirebaseDatabase.getInstance().reference.child("TimeTableLinks").child("ECE")
            eceReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    link = dataSnapshot.value.toString()
                    Log.d(ContentValues.TAG, "links for ECE is $link")
                    webView()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
}