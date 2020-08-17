package com.cherry.yandexmusictospotify

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaquo.python.Python
import kotlinx.android.synthetic.main.fragment_yandex_music.*

class YandexMusicFragment : Fragment() {
    private lateinit var container: ViewGroup
    private lateinit var sp : SharedPreferences
    private lateinit var recycler : RecyclerView
    private lateinit var adapter : YandexMusicAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.container = container!!
        return inflater.inflate(R.layout.fragment_yandex_music, container, false)
    }

    override fun onStart() {
        super.onStart()
        sp = activity!!.getSharedPreferences ("Password", Context.MODE_PRIVATE)

        recycler = container.findViewById (R.id.yandex_music_recycler)
        adapter = YandexMusicAdapter ()
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager (activity!!)

        swipe_refresh_ya_music.setOnRefreshListener {
            refresh ()
        }
        val s = sp.getString ("saved_ya_music", null)
        if (s != null) {
            val a = kek (s)
            adapter.addItem (a)
        } else {
            refresh()
        }

    }

    fun refresh () {
        swipe_refresh_ya_music.isRefreshing = true
        MyTask ().execute ()
    }

    fun lol (s : String) : YaMusicItem {
        var title = ""
        var artists = ""
        var uri = ""
        var i = 0
        while (s[i] != '~') {
            title += s[i++]
        }
        ++i
        ++i
        while (s[i] != '~') {
            artists += s[i++]
        }
        ++i
        ++i
        while (i < s.length) uri += s[i++]
        return YaMusicItem (title, artists, uri)
    }

    fun kek (s : String) : ArrayList<YaMusicItem> {
        val a : ArrayList<YaMusicItem> = arrayListOf ()
        var i = 0
        var t = ""
        while (i < s.length) {
            when {
                s[i] == '}' -> {
                    ++i
                    a.add (lol (t))
                    t = ""
                }
                s[i] == '{' -> {
                    ++i
                }
                else -> {
                    t += s[i]
                    ++i
                }
            }
        }
        return a
    }

    inner class MyTask : AsyncTask<Void, Void, Void>() {
        var a : ArrayList<YaMusicItem> = arrayListOf ()
        var flag = false

        override fun doInBackground(vararg params: Void?): Void? {
            adapter.clearItem ()
            val user = sp.getString ("yandex_music_user", null)
            val pass = sp.getString ("yandex_music_password", null)
            if (user == null) {
                return null
            }
            val python = Python.getInstance ()
            val pythonFile = python.getModule ("kek")
            val s = pythonFile.callAttr ("our_func", user, pass).toString ()
            sp.edit ().putString ("saved_ya_music", s).apply ()
            a = kek (s)
            flag = true
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
//            for (x in a) {
//                Log.e ("kek", x.toString ())
//            }
            if (flag) {
                adapter.addItem(a)
            } else {
                Toast.makeText (activity, "Давайте войдем в аккаунт", Toast.LENGTH_SHORT).show ()
            }
            swipe_refresh_ya_music.isRefreshing = false
        }
    }

}