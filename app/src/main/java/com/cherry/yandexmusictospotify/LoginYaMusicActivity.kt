package com.cherry.yandexmusictospotify

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.chaquo.python.Python
import kotlinx.android.synthetic.main.fragment_yandex_music.*
import java.lang.Exception

class LoginYaMusicActivity : AppCompatActivity() {
    private lateinit var user : EditText
    private lateinit var password : EditText
    private lateinit var btn : Button
    private lateinit var sp : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_ya_music)
        user = findViewById (R.id.login)
        password = findViewById (R.id.password)
        btn = findViewById (R.id.log_in)
        sp = getSharedPreferences ("Password", Context.MODE_PRIVATE)

        btn.setOnClickListener {
            if (user.text.isEmpty () || password.text.isEmpty ()) {
                Toast.makeText (this, "Заполните все поля", Toast.LENGTH_SHORT).show ()
            } else {
                ThatTask ().execute ()
            }
        }
    }

    inner class ThatTask : AsyncTask<Void, Void, Void>() {
        private lateinit var u : String
        private lateinit var p : String
        private var flag = false

        override fun onPreExecute() {
            super.onPreExecute()
            u = user.text.toString ()
            p = password.text.toString ()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            val python = Python.getInstance ()
            val pythonFile = python.getModule ("kek")
            try {
                val s = pythonFile.callAttr("our_func", u, p).toString()
                sp.edit ().putString ("saved_ya_music", s).apply ()
                flag = true
            } catch (e : Exception) {
                Log.e ("kek", e.toString ())
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            if (flag) {
                sp.edit().putString("yandex_music_user", u).apply()
                sp.edit().putString("yandex_music_password", p).apply()
                finish ()
            } else {
                Toast.makeText (this@LoginYaMusicActivity, "Что-то пошло не так", Toast.LENGTH_SHORT).show ()
            }
        }

    }
}