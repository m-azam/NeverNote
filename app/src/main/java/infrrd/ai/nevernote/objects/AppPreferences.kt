package infrrd.ai.nevernote.objects

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {
    private const val NAME = "NeverNote"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    // list of app specific preferences
    private const val IS_LOGIN_VALID = "is_first_run"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var loginValidity: Boolean
    // custom getter to get a preference of a desired type, with a predefined default value

        get() = preferences.getBoolean(IS_LOGIN_VALID, false)

    // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putBoolean(IS_LOGIN_VALID, value)
        }
}