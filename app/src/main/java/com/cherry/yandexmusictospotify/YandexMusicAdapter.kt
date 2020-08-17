package com.cherry.yandexmusictospotify

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class YandexMusicAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val listItem = arrayListOf<YaMusicItem>()
    lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from (parent.context).inflate (R.layout.ya_music_holder, parent, false)
        context = parent.context
        return YaMusicHolder (view)
    }

    fun addItem (item : YaMusicItem) {
        listItem.add (item)
        notifyItemChanged (itemCount - 1)
    }

    fun addItem (a : ArrayList<YaMusicItem>) {
        for (x in a) listItem.add (x)
        notifyDataSetChanged ()
    }

    fun clearItem () {
        listItem.clear ()
        notifyDataSetChanged ()
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as YaMusicHolder).bind (listItem[position])
    }

    inner class YaMusicHolder (itemView: View) : RecyclerView.ViewHolder (itemView) {
        val title : TextView = itemView.findViewById (R.id.title)
        val artists : TextView = itemView.findViewById (R.id.artists)
        val image : ImageView = itemView.findViewById (R.id.image)
        val checkBox : CheckBox = itemView.findViewById (R.id.checkbox)

        fun bind (item : YaMusicItem) {
            title.text = item.title
            artists.text = item.name
            Glide.with (context).load (item.uri).into (image)
        }

    }
}