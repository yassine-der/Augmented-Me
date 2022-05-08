package com.example.sifflet0.api

import android.content.Context
import android.content.SharedPreferences
import com.example.augmentedme.PREF_NAME

class SessionManager (context: Context?) {

    private var prefs: SharedPreferences = context!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        const val TOKEN = "TOKEN"
    }

    /**
     * Function to save auth token
     */
    /*
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }
    */


    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(TOKEN, null)
    }


}
