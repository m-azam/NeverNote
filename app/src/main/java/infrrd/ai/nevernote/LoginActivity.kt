package infrrd.ai.nevernote

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import infrrd.ai.nevernote.objects.AppPreferences
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login_button.setOnClickListener {
            validateSignIn()
        }
    }

    private fun validateSignIn() {
        val passwordValidity = validatePassword()
        if (validateUsername() && passwordValidity) {
            /*Call API here*/
            AppPreferences.loginValidity = true
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

    private fun validateUsername(): Boolean {
        if (email_id.text.isNullOrEmpty())
        {
            email_id.error = "Field can't be left empty"
            return false
        }
        else if (!Pattern.matches(".+@[a-zA-Z]+\\.com", email_id.text)) {
            email_id.error = "Not a valid email"
            return false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (password.text.isNullOrEmpty()) {
            password.error = "This field can't be empty"
            return false
        }
        return true
    }
}