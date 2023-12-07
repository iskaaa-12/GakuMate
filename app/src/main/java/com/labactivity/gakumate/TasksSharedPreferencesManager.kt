package com.labactivity.gakumate

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labactivity.gakumate.Tasks

class TasksSharedPreferencesManager(context: Context, categoryName: String? = null) {
    private val sharedPreferences: SharedPreferences
    private val gson = Gson()

    // Use a different preference key for each category
    private val PREFS_NAME = "MyPrefsFile_${categoryName ?: "DefaultCategory"}"
    private val TASKS_KEY = "tasks_key"

    init {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }


    fun saveTasks(tasks: ArrayList<Tasks>) {
        val editor = sharedPreferences.edit()
        val tasksJson = gson.toJson(tasks)
        editor.putString(TASKS_KEY, tasksJson) // Use TASKS_KEY here
        editor.apply()
    }

    fun getTasks(): ArrayList<Tasks> {
        val tasksJson = sharedPreferences.getString(TASKS_KEY, "")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Tasks>>() {}.type
        return gson.fromJson(tasksJson, type) ?: ArrayList()
    }
}