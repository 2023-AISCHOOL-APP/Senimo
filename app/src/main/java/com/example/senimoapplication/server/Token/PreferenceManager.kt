package com.example.senimoapplication.server.Token

import android.content.Context
import com.google.gson.Gson

// PreferenceManager.kt
object PreferenceManager {
    private const val PREFS_NAME = "token_prefs"
    private const val ACCESS_TOKEN_KEY = "access_token"
    private const val REFRESH_TOKEN_KEY = "refresh_token"
    private const val USER_DATA_KEY = "user_data"

    fun setAccessToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(ACCESS_TOKEN_KEY, token).apply()
    }

    fun getAccessToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    fun setRefreshToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(REFRESH_TOKEN_KEY, token).apply()
    }

    fun getRefreshToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
    }

    // UserDatas 객체 저장
    fun setUser(context: Context, userData: UserDatas) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(userData)
        editor.putString(USER_DATA_KEY, json)
        editor.apply()
    }

    // UserDatas 객체 불러오기
    fun getUser(context: Context): UserDatas? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(USER_DATA_KEY, null)
        return if (json != null) gson.fromJson(json, UserDatas::class.java) else null
    }

    // 토큰 제거 메소드
    fun clearToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().remove(ACCESS_TOKEN_KEY).remove(REFRESH_TOKEN_KEY).apply()
    }
}

