import android.content.Context
import android.content.SharedPreferences

object YolcSharedPreferences {
    private val ACCOUNT: String = "account"

    fun setAddress(context: Context, input: String){
        val prefs: SharedPreferences = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString("address", input)
        editor.commit()
    }

    fun getAddress(context: Context): String{
        val prefs: SharedPreferences = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("address", "").toString()
    }

    fun setToken(context: Context, input: String){
        val prefs: SharedPreferences = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString("token", input)
        editor.commit()
    }

    fun getToken(context: Context): String{
        val prefs: SharedPreferences = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("token", "").toString()
    }

    fun setUserId(context: Context, input: String){
        val prefs: SharedPreferences = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString("user_id", input)
        editor.commit()
    }

    fun getUserId(context: Context): String{
        val prefs: SharedPreferences = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("user_id", "").toString()
    }

    fun setUserPass(context: Context, input: String){
        val prefs: SharedPreferences = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString("user_pass", input)
        editor.commit()
    }

    fun getUserPass(context: Context): String{
        val prefs: SharedPreferences = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("user_pass","").toString()
    }

    fun clearUser(context: Context){
        val prefs: SharedPreferences = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.commit()
    }
}