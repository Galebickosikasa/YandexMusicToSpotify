package com.cherry.yandexmusictospotify

class MusicItem : Comparable<MusicItem> {
    val title : String
    val name : String
    val uri : String
    val time : String

    constructor(title: String, name: String, uri: String) {
        this.title = title
        this.name = name
        this.uri = uri
        this.time = ""
    }

    constructor(title: String, name: String, uri: String, time: String) {
        this.title = title
        this.name = name
        this.uri = uri
        this.time = time
    }

    override fun compareTo(other: MusicItem): Int {
        return time.compareTo (other.time)
    }

    override fun toString(): String {
        return "YaMusicItem(title='$title', name='$name', uri='$uri')"
    }

}