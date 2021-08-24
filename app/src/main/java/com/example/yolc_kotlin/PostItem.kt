package com.example.yolc_kotlin

class PostItem {
    private var title: String? = null
    private var text: String? = null

    public fun getText(): String? {
        return text
    }

    public fun getTitle(): String?{
        return title
    }

    public fun setTitle(s: String){
        title = s
    }

    public fun setText(s: String){
        text = s
    }
}