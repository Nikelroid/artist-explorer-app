package com.nikelroid.artist_pedia
import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class TimeUtils (absDataAndTime: MutableList<String>){
    private val currentTime = Date()
    private var absDataAndTime: MutableList<String> = mutableListOf()
    var isAlive: Boolean = true

    init {
        isAlive = true
        this.absDataAndTime = absDataAndTime
    }


    fun getFavTime(dateAndTime: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        val creatingTime = try {
            dateFormat.parse(dateAndTime) ?: Date()
        } catch (e: Exception) {
            Date()
        }

        return getFavTime(creatingTime)
    }



    fun getUpdatedFavTime(): MutableList<String>{
        updateCurrentTime()
        var relDataAndTime: MutableList<String> = mutableListOf()
        for (index in this.absDataAndTime.indices){
            relDataAndTime.add(getFavTime(this.absDataAndTime[index]))
            }
        return relDataAndTime
    }

    fun getFavTime(dateAndTime: Date): String {
        val diff = currentTime.time - dateAndTime.time
        val seconds = (diff / 1000).toInt()

        return when {
            seconds <= 0 -> {
                    "Just now"
            }
            seconds <= 60 -> {
                if (seconds == 1) {
                    "$seconds second ago"
                } else {
                    "$seconds seconds ago"
                }
            }
            seconds <= 3600 -> {
                val minutes = seconds / 60
                if (minutes == 1) {
                    "$minutes minute ago"
                } else {
                    "$minutes minutes ago"
                }
            }
            seconds <= 86400 -> {
                val hours = seconds / 3600
                if (hours == 1) {
                    "$hours hour ago"
                } else {
                    "$hours hours ago"
                }
            }
            else -> {
                val days = seconds / 86400
                if (days == 1) {
                    "$days day ago"
                } else {
                    "$days days ago"
                }
            }
        }
    }

    fun updateCurrentTime() {
        currentTime.time = System.currentTimeMillis()
    }

}
