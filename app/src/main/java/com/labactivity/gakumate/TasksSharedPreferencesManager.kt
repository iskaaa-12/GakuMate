import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labactivity.gakumate.Tasks

class TasksSharedPreferencesManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("tasks_shared_prefs", Context.MODE_PRIVATE)
    }

    fun saveTasks(tasks: ArrayList<Tasks>) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val tasksJson = gson.toJson(tasks)
        editor.putString("tasks", tasksJson)
        editor.apply()
    }

    fun getTasks(): ArrayList<Tasks> {
        val tasksJson = sharedPreferences.getString("tasks", "")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Tasks>>() {}.type
        return gson.fromJson(tasksJson, type) ?: ArrayList()
    }
}
