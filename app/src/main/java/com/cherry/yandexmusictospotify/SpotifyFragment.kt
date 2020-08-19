package com.cherry.yandexmusictospotify

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_spotify.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class SpotifyFragment : Fragment() {
    private lateinit var container: ViewGroup
    private lateinit var recycler : RecyclerView
    private lateinit var adapter : SpotifyAdapter
    private lateinit var mAccessToken : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.container = container!!
        return inflater.inflate(R.layout.fragment_spotify, container, false)
    }

    override fun onStart() {
        super.onStart()
        recycler = container.findViewById (R.id.spotify_recycler)
        recycler.layoutManager = LinearLayoutManager (activity!!)
        adapter = SpotifyAdapter ()
        recycler.adapter = adapter
        swipe_refresh_spotify.setOnRefreshListener {
            refresh ()
        }
        if ((activity!! as MainActivity).lastLoadSpotify.isEmpty ()) refresh ()
        else {
            adapter.clearItems ()
            adapter.addItem ((activity!! as MainActivity).lastLoadSpotify)
        }
    }

    fun refresh () {
        swipe_refresh_spotify.isRefreshing = true
        adapter.clearItems ()
        mAccessToken = activity!!.getSharedPreferences("Password", Context.MODE_PRIVATE).getString("Token", "LOL")!!
        val my_list : ArrayList<MusicItem> = arrayListOf ()
        var skipped = 0
        val request = Request.Builder ()
            .url ("https://api.spotify.com/v1/me/tracks?limit=1")
            .addHeader ("Authorization", "Bearer $mAccessToken")
            .build ()
        val call = OkHttpClient ().newCall (request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {
                val tmp = JSONObject (response.body!!.string ())
//                Log.e ("kek", tmp.toString())
                val total = tmp.getString ("total").toInt ()
                while (skipped < total) {
                    val request1 = Request.Builder ()
                        .url ("https://api.spotify.com/v1/me/tracks?limit=50&offset=$skipped")
                        .addHeader ("Authorization", "Bearer $mAccessToken")
                        .build ()
                    val call1 = OkHttpClient ().newCall(request1)

                    call1.enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {}

                        override fun onResponse(call: Call, response: Response) {
                            val jsonObject = JSONObject (response.body!!.string())
                            val a = jsonObject.getJSONArray("items")
                            for (i in 0 until a.length()) {
                                var title = ""
                                var artists = ""
                                var uri = ""
                                val time = (a[i] as JSONObject).getString ("added_at")
                                var x = (a[i] as JSONObject).getJSONObject("track")
                                title = x.getString("name")
                                var b = x.getJSONArray("artists")
                                for (j in 0 until b.length()) {
                                    artists += (b[j] as JSONObject).getString("name") + ", "
                                }
                                x = x.getJSONObject("album")
                                b = x.getJSONArray("images")
                                uri = (b[0] as JSONObject).getString("url")
                                artists = artists.dropLast (2)
                                my_list.add (MusicItem (title, artists, uri, time))
                            }
                            if (my_list.size == total) {
                                my_list.sort ()
                                my_list.reverse ()
                                (activity!! as MainActivity).lastLoadSpotify = my_list
                                activity!!.runOnUiThread {
                                    adapter.addItem(my_list)
                                }
                                swipe_refresh_spotify.isRefreshing = false
                            }
                        }
                    })
                    skipped += 50
                }
            }
        })

    }

}