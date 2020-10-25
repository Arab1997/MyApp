package myway.myapplication.app

import myway.myapplication.BuildConfig


object Constants {

    const val BASE_URL = BuildConfig.BASE_URL
    const val TIMEOUT = 40.toLong()

    const val PLAYING = 12134

    const val MESSAGE_MY = 100
    const val MESSAGE_SERVER = 101
    const val MESSAGE_CENTER = 102
    const val IMAGE_MY = 103
    const val IMAGE_SERVER = 104

    const val COMMENT_WITH_FILE = 105
    const val COMMENT_MSG = 106

    const val DAILY_CONTENT_TYPE_MORNING_VIP = "morning_vip"
    const val DAILY_CONTENT_TYPE_DAY_VIP = "day_vip"
    const val DAILY_CONTENT_TYPE_DAY = "day"
    const val DAILY_CONTENT_TYPE_EVENING_VIP = "evening_vip"

    const val RADIO = "single"
    const val CHECKBOX = "multiple"
    const val TEXT = "text"

    const val NEWS_DETAILED_FRAGMENT = "NEWS_DETAILED_FRAGMENT"

 /*   val colors = arrayListOf(
        R.color.orange,
        R.color.red,
        R.color.green,
        R.color.blue,
        R.color.violet,
        R.color.yellow
    )*/
}

data class KeyValue(val key: String, val value: String)
