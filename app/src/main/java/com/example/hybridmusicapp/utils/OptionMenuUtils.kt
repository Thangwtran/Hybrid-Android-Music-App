package com.example.hybridmusicapp.utils

import android.view.MenuItem

object OptionMenuUtils {
    private val menuItems:MutableList<MenuItem> = ArrayList()

    init {
        createMainMenuItems()
    }

    private fun createMainMenuItems() {
        TODO("Not yet implemented")
    }

    enum class OptionMenu(val value:String){
        DOWNLOAD("download"),
        FAVORITE("favorite"),
        ADD_PLAYLIST("add_playlist"),
        PLAY_NEXT("play_next"),
        SEE_ALBUM("see_album"),
        SEE_ARTIST("see_artist"),
        BLOCK("block"),
        ERROR("error"),
        INFO("info")
    }
}