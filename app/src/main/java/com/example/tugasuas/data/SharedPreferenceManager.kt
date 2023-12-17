import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    companion object {
        const val USERNAME_KEY = "username"
        const val PASSWORD_KEY = "password"
        const val ROLE_KEY = "role"
        const val IS_LOGGED_IN_KEY = "isLoggedIn"
    }

    fun saveLoginDetails(username: String, password: String, role: String) {
        val editor = sharedPref.edit()
        editor.putString(USERNAME_KEY, username)
        editor.putString(PASSWORD_KEY, password)
        editor.putString(ROLE_KEY, role)
        editor.putBoolean(IS_LOGGED_IN_KEY, true)
        editor.apply()
    }

    fun getSavedRole(): String? {
        return sharedPref.getString(ROLE_KEY, null)
    }

    fun isLoggedIn(): Boolean {
        return sharedPref.getBoolean(IS_LOGGED_IN_KEY, false)
    }

    fun clearLoginDetails() {
        val editor = sharedPref.edit()
        editor.remove(USERNAME_KEY)
        editor.remove(PASSWORD_KEY)
        editor.remove(ROLE_KEY)
        editor.putBoolean(IS_LOGGED_IN_KEY, false)
        editor.apply()
    }
}
