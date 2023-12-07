import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.labactivity.gakumate.TheCategory

class DatabaseHelper(context: Context) {

    private val sharedPreferences: SharedPreferences
    private val gson: Gson = Gson()

    init {
        // Initialize SharedPreferences
        sharedPreferences = context.getSharedPreferences("CatDatabase", Context.MODE_PRIVATE)
    }

    fun saveCategories(cats: ArrayList<TheCategory>) {
        val json = gson.toJson(cats)
        sharedPreferences.edit().putString("categories", json).apply()
    }

    fun getCategories(): ArrayList<TheCategory> {
        val json = sharedPreferences.getString("categories", null)
        val type = object : TypeToken<ArrayList<TheCategory>>() {}.type
        return gson.fromJson(json, type) ?: ArrayList()
    }
}
