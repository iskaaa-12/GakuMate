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
        val gson = Gson()
    }

    fun saveCat(cats: ArrayList<TheCategory>) {
        // Convert ArrayList to JSON using Gson
        val json = gson.toJson(cats)

        // Save JSON to SharedPreferences
        sharedPreferences.edit().putString("categories", json).apply()
    }

    fun getCategories(): ArrayList<TheCategory> {
        val json = sharedPreferences.getString("categories", null)
        val type = object : TypeToken<ArrayList<TheCategory>>() {}.type
        return gson.fromJson(json, type) ?: ArrayList()
    }
}
