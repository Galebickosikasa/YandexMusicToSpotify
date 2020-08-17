package com.cherry.yandexmusictospotify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SpotifyFragment : Fragment() {
    private lateinit var container: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.container = container!!
        return inflater.inflate(R.layout.fragment_spotify, container, false)
    }

    override fun onStart() {
        super.onStart()

    }

}