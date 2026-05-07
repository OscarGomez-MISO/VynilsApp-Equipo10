package com.example.vynilsappequipo10.ui.main

import android.content.Context
import android.content.SharedPreferences

class UserSession(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("vynils_prefs", Context.MODE_PRIVATE)

    fun saveCollector(id: Int, email: String) {
        prefs.edit().putInt("collector_id", id).putString("collector_email", email).commit()
    }

    fun getCollectorId(): Int = prefs.getInt("collector_id", -1)
    fun getCollectorEmail(): String? = prefs.getString("collector_email", null)

    fun clear() {
        prefs.edit().clear().commit()
    }
}
