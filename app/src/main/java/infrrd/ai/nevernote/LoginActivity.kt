package infrrd.ai.nevernote

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import infrrd.ai.nevernote.objects.AppPreferences
import java.util.regex.Pattern
import kotlinx.android.synthetic.main.activity_login.register_button
import kotlinx.android.synthetic.main.activity_login.login_email_text_input_layout
import kotlinx.android.synthetic.main.activity_login.login_password_text_input_layout
import kotlinx.android.synthetic.main.activity_login.login_button
import kotlinx.android.synthetic.main.activity_login.email_id
import kotlinx.android.synthetic.main.activity_login.password

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        editTextOnFocusChangeListener(login_email_text_input_layout, email_id, this::validateUsername)
        editTextOnFocusChangeListener(login_password_text_input_layout, password, this::validatePassword)
        login_button.setOnClickListener {
            validateSignIn()
        }
        register_button.setOnClickListener {
            openSignupScreen()
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
        if (email_id.text.isNullOrEmpty()) {
            login_email_text_input_layout.isErrorEnabled = true
            login_email_text_input_layout.error = "Field can't be left empty"
            return false
        }
        else if (!validateEmail(email_id.text.toString())) {
            login_email_text_input_layout.isErrorEnabled = true
            login_email_text_input_layout.error = "Not a valid email"
            return false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (password.text.isNullOrEmpty()) {
            login_password_text_input_layout.isErrorEnabled = true
            login_password_text_input_layout.error = "This field can't be empty"
            return false
        }
        return true
    }

    fun openSignupScreen(){
        val signupActivityIntent = Intent(this, SignupActivity::class.java)
        startActivity(signupActivityIntent)
    }

    fun editTextOnFocusChangeListener(textInputLayout: TextInputLayout, textInputEditText: TextInputEditText, callback: () -> Boolean){
        textInputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                textInputLayout.error = ""
                textInputLayout.isErrorEnabled = false
            }
            else
                callback()
        }
    }
}
