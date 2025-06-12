package com.example.hybridmusicapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object MusicAppUtils {
    var sConfigChanged: Boolean = false
    var DENSITY: Float = 0f

    fun isNetworkAvailable(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = manager.activeNetwork ?: return false
        val networkCapabilities = manager.getNetworkCapabilities(network)
        return (networkCapabilities != null
                && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)))
    }

    @JvmStatic
    fun getCurrentPlayTime(duration: Long, angle: Float): Long {
        return (duration * angle / 360).toLong()
    }

    enum class DefaultPlaylistName(val value: String) {
        DEFAULT("default"),  // 0
        FAVORITE("favorite"),  // 1
        RECOMMENDED("recommended"),  // 2
        RECENT("recent"),  // 3
        SEARCHED("searched"),  // 4
        MOST_HEARD("most_heard"),  // 5
        CUSTOM("custom"),  // 6
        FOR_YOU("for_you"), // 7
        NCS_SONG("ncs"),// 8
        RECENT_NCS("recent_ncs"),  // 9
        FAVORITE_NCS("favorite_ncs"),  // 10


    }
}