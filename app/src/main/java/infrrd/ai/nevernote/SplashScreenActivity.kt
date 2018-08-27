package infrrd.ai.nevernote

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            nextScreenLoginCheck()
        }, 1000)
    }

    private fun nextScreenLoginCheck() {
        val flag: Boolean = true
        val intent = if(flag) Intent(this, MainActivity::class.java) else Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}
