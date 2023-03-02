package com.example.watch.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ui.custom_view.WatchView
import com.example.watch.R

class WatchFragment : Fragment() {

    private var watchView: WatchView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_watch, container, false)

        watchView = view.findViewById(R.id.watch_view)

        return view
    }

    companion object {
        fun newInstance() = WatchFragment()
    }
}