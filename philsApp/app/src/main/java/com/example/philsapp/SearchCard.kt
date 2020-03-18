package com.example.philsapp

class SearchCard {
    var title: String? = null
    var years: String? = null
    var type: String? = null

    constructor(title: String?, type: String?, years: String?) {
        this.title = title
        this.years = years
        this.type = type
    }
}