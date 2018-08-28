package infrrd.ai.nevernote

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_signup.signup_button

class SignupActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        signup_button.setOnClickListener {
//            Write API for signup here
            goToLoginScreen()
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_signup
    }

    fun goToLoginScreen(){
        finish()
    }
}
