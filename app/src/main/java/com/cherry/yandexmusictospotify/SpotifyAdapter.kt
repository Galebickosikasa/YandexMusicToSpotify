package com.cherry.yandexmusictospotify

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SpotifyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listItem : ArrayList<MusicItem> = arrayListOf ()
    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spotify_music_holder, parent, false)
        context = parent.context
        return SpotifyViewHolder (view)
    }

    fun addItem (item : MusicItem) {
        listItem.add (item)
        notifyItemChanged (itemCount - 1)
    }

    fun addItem (new_list : ArrayList<MusicItem>) {
        for (x in new_list) listItem.add (x)
        notifyDataSetChanged ()
    }

    fun clearItems () {
        listItem.clear ()
        notifyDataSetChanged ()
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SpotifyViewHolder).bind (listItem[position])
    }

    inner class SpotifyViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        private var title : TextView = itemView.findViewById (R.id.title)
        private var artists : TextView = itemView.findViewById (R.id.artists)
        private var image : ImageView = itemView.findViewById (R.id.image)

        fun bind (item : MusicItem) {
            title.text = item.title
            artists.text = item.name
            Glide.with (context).load (item.uri).into (image)
        }
    }
}