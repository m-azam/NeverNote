package infrrd.ai.nevernote

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_signup.signup_email_id_text_input_layout
import kotlinx.android.synthetic.main.activity_signup.signup_email_id_text_input
import kotlinx.android.synthetic.main.activity_signup.signup_password_text_input_layout
import kotlinx.android.synthetic.main.activity_signup.signup_password_text_input
import kotlinx.android.synthetic.main.activity_signup.signup_confirm_password_text_input_layout
import kotlinx.android.synthetic.main.activity_signup.signup_confirm_password_text_input
import kotlinx.android.synthetic.main.activity_signup.signup_button
import kotlinx.android.synthetic.main.activity_signup.close_signup_screen
import java.util.regex.Pattern

class SignupActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        close_signup_screen.setOnClickListener {
            goToLoginScreen()
        }

        textInputOnFocusChangeListener(signup_email_id_text_input_layout, signup_email_id_text_input, this::validateEmail)
        textInputOnFocusChangeListener(signup_password_text_input_layout, signup_password_text_input, this::confirmPassword)
        textInputOnFocusChangeListener(signup_confirm_password_text_input_layout, signup_confirm_password_text_input, this::confirmPassword)

        signup_button.setOnClickListener {
            var emailValidatorFlag = confirmPassword()
            var passwordValidatorFlag = validateEmail()
            if(emailValidatorFlag && passwordValidatorFlag){
//                API for signup here
                goToLoginScreen()
            }
        }
    }

    fun goToLoginScreen(){
        finish()
    }

    fun confirmPassword(): Boolean {
        var returnValue = true;
        if(signup_password_text_input.text.isNullOrEmpty()) {
            signup_password_text_input_layout.isErrorEnabled = true
            signup_password_text_input_layout.error = "Field can't be left empty"
            returnValue = false
        }
        if(signup_confirm_password_text_input.text.isNullOrEmpty()) {
            signup_confirm_password_text_input_layout.isErrorEnabled = true
            signup_confirm_password_text_input_layout.error = "Field can be left empty"
            returnValue = false
        }
        if(returnValue) return signup_password_text_input.text == signup_confirm_password_text_input.text else return false
    }

    fun validateEmail(): Boolean {
        if (signup_email_id_text_input.text.isNullOrEmpty()) {
            signup_email_id_text_input_layout.isErrorEnabled = true
            signup_email_id_text_input_layout.error = "Field can't be left empty"
            return false
        }
        else if (!Pattern.matches(".+@[a-zA-Z]+\\.com", signup_email_id_text_input.text)) {
            signup_email_id_text_input_layout.isErrorEnabled = true
            signup_email_id_text_input_layout.error = "Not a valid email"
            return false
        }
        return true
    }
    
    fun textInputOnFocusChangeListener(textInputLayout: TextInputLayout, textInputEditText: TextInputEditText, callback: () -> Boolean){
        textInputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                textInputLayout.error = null
                textInputLayout.isErrorEnabled = false
            }
            else
                callback()
        }
    }

}
