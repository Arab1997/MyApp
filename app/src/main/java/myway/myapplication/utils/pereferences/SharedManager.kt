package myway.myapplication.utils.pereferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class SharedManager(
    private val preferences: SharedPreferences,
    private val gson: Gson,
    context: Context
) {

    val default = PreferenceHelper.defaultPrefs(context)

    companion object {
        const val TOKEN = "TOKEN"
        const val CODE = "CODE"
        const val LANGUAGE = "LANGUAGE"
        const val USER = "USER"
        const val FINISHED_TASKS = "FINISHED_TASKS"
    }

    fun deleteAll() {
        preferences.edit().clear().apply()
    }
}
