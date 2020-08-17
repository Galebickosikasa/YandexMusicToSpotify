package com.cherry.yandexmusictospotify

class YaMusicItem {
    val title : String
    val name : String
    val uri : String

    constructor(title: String, name: String, uri: String) {
        this.title = title
        this.name = name
        this.uri = uri
    }

    override fun toString(): String {
        return "YaMusicItem(title='$title', name='$name', uri='$uri')"
    }


}