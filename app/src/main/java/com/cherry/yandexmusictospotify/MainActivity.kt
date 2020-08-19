package com.cherry.yandexmusictospotify

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.cherry.yandexmusictospotify.Constants.CLIENT_ID
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class MainActivity : AppCompatActivity(), YandexMusicAdapter.OnYandexMusicClick {
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var sp : SharedPreferences
    private lateinit var yandexMusicFragment : YandexMusicFragment
    private lateinit var spotifyFragment : SpotifyFragment
    private lateinit var toolbar : Toolbar
    private var mAccessToken : String? = null
    private val AUTH_TOKEN_REQUEST_CODE = 0x10
    var lastLoadSpotify : ArrayList<MusicItem> = arrayListOf ()
    var kekList : ArrayList<MusicItem> = arrayListOf ()
    private lateinit var goBtn : View

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
        goBtn = findViewById (R.id.goBtn)
        goBtn.isVisible = false

        toolbar = findViewById (R.id.toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.add_yandex_music -> {
                    startActivity (Intent (Constants.LOGIN_YA_MUSIC_ACTIVITY_PATH))
                }
                R.id.add_spotify -> {
                    val request: AuthorizationRequest = getAuthenticationRequest (AuthorizationResponse.Type.TOKEN)!!
                    AuthorizationClient.openLoginActivity (this, AUTH_TOKEN_REQUEST_CODE, request)
                }
                R.id.goBtn -> {
                    clickGoBtn ()
                }
            }
            false
        }

    }

    override fun click(item: MusicItem) {
        Log.e ("kek", item.toString())
        if (kekList.contains(item)) {
            kekList.remove(item)
            if (kekList.isEmpty()) goBtn.isVisible = false
        }
        else {
            kekList.add (item)
            if (kekList.size == 1) goBtn.isVisible = true
        }
    }

    fun clickGoBtn () {
        Log.e ("kek", "tap")
        val t : ArrayList<String> = arrayListOf ()
        for (x in kekList) {
            
        }
    }

    override fun onStart() {
        super.onStart()
        val request: AuthorizationRequest = getAuthenticationRequest (AuthorizationResponse.Type.TOKEN)!!
        AuthorizationClient.openLoginActivity (this, AUTH_TOKEN_REQUEST_CODE, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = AuthorizationClient.getResponse(resultCode, data)
        when (requestCode) {
            AUTH_TOKEN_REQUEST_CODE -> {
                mAccessToken = response.accessToken
                Log.e ("kek", "$mAccessToken")
                sp.edit ().putString ("Token", mAccessToken).apply ()
            }
        }
    }

    private fun getAuthenticationRequest(type: AuthorizationResponse.Type): AuthorizationRequest? {
        return AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
            .setShowDialog(false)
            .setScopes(arrayOf("user-read-email", "user-library-read"))
            .setCampaign("your-campaign-token")
            .build()
    }

    private fun getRedirectUri(): Uri? {
        return Uri.Builder()
            .scheme("spotify-sdk")
            .authority("auth")
            .build()
    }
}