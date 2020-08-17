package com.cherry.yandexmusictospotify

import androidx.fragment.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var sp : SharedPreferences
    private lateinit var yandexMusicFragment : YandexMusicFragment
    private lateinit var spotifyFragment : SpotifyFragment
    private lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById (R.id.bottom_nav)
        sp = getSharedPreferences ("Password", Context.MODE_PRIVATE)
        yandexMusicFragment = YandexMusicFragment ()
        spotifyFragment = SpotifyFragment ()
        if (!Python.isStarted ()) {
            Python.start (AndroidPlatform (this))
        }

        supportFragmentManager.beginTransaction ().replace (R.id.fragment_container, yandexMusicFragment).commit ()

        bottomNavigationView.setOnNavigationItemSelectedListener {
            var fragment : Fragment? = null
            when (it.itemId) {
                R.id.yandex_music -> {
                    fragment = yandexMusicFragment
                }
                R.id.spotify -> {
                    fragment = spotifyFragment
                }
            }
            supportFragmentManager.beginTransaction ().replace (R.id.fragment_container, fragment!!).commit ()
            return@setOnNavigationItemSelectedListener true
        }

        toolbar = findViewById (R.id.toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_yandex_music -> {
                    startActivity (Intent (Constants.LOGIN_YA_MUSIC_ACTIVITY_PATH))
                }
                else -> {
                    Log.e ("kek", "lol")
                }
            }
            false
        }

    }
}