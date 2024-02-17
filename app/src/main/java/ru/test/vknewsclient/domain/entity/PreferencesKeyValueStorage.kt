package ru.test.vknewsclient.domain.entity

import android.content.Context

class PreferencesKeyValueStorage(context: Context, prefsName: String = PREFERENCE_NAME) {
    private val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    fun put(key: String, value: String){
        prefs.edit().putString(key, value).apply()
    }

    fun get(key: String): String? {
        return prefs.getString(key, null)
    }

    companion object {
        private const val PREFERENCE_NAME = "creds_store"
    }
}