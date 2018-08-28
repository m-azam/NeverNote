package infrrd.ai.nevernote

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.signup_email_id
import kotlinx.android.synthetic.main.activity_signup.signup_button
import kotlinx.android.synthetic.main.activity_signup.signup_password
import kotlinx.android.synthetic.main.activity_signup.signup_confirm_password
import java.util.regex.Pattern

class SignupActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        signup_button.setOnClickListener {
            var emailValidatorFlag = confirmPassword()
            var passwordValidatorFlag = validateEmail()
            if(emailValidatorFlag && passwordValidatorFlag){
//                API for signup here
                goToLoginScreen()
            }
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_signup
    }

    fun goToLoginScreen(){
        finish()
    }

    fun confirmPassword(): Boolean {
        var returnValue = true;
        if(signup_password.text.isNullOrEmpty()) {
            signup_password.error = "Field can't be left empty"
            returnValue = false
        }
        if(signup_confirm_password.text.isNullOrEmpty()) {
            signup_confirm_password.error = "Field can be left empty"
            returnValue = false
        }
        if(returnValue) return signup_password.text == signup_confirm_password.text else return false   
    }

    fun validateEmail(): Boolean {
        if (signup_email_id.text.isNullOrEmpty()) {
            signup_email_id.error = "Field can't be left empty"
            return false
        }
        else if (!Pattern.matches(".+@[a-zA-Z]+\\.com", signup_email_id.text)) {
            signup_email_id.error = "Not a valid email"
            return false
        }
        return true
    }
}
