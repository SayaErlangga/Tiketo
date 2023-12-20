import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    companion object {
        const val USERNAME_KEY = "username"
        const val PASSWORD_KEY = "password"
        const val EMAIL_KEY = "email"
        const val PHONE_KEY = "phone"
        const val ROLE_KEY = "role"
        const val IS_LOGGED_IN_KEY = "isLoggedIn"
    }

    fun saveLoginDetails(username: String, password: String, role: String, email: String, phone: String) {
        val editor = sharedPref.edit()
        editor.putString(USERNAME_KEY, username)
        editor.putString(PASSWORD_KEY, password)
        editor.putString(ROLE_KEY, role)
        editor.putString(EMAIL_KEY, email)
        editor.putString(PHONE_KEY, phone)
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

    // Inside the SharedPreferenceManager class
    fun getUserEmail(): String? {
        return sharedPref.getString(EMAIL_KEY, null)
    }
    // Inside SharedPreferenceManager
    fun getSavedUserName(): String? {
        return sharedPref.getString(USERNAME_KEY, null)
    }
    fun getSavedUserPhone(): String? {
        return sharedPref.getString(PHONE_KEY, null)
    }
}
