package infrrd.ai.nevernote

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import infrrd.ai.nevernote.objects.AppPreferences

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        AppPreferences.init(this)
        VolleyConfig.init(applicationContext)
        Handler().postDelayed({
            nextScreenLoginCheck()
        }, 1000)
    }

    private fun nextScreenLoginCheck() {
        val intent = if (AppPreferences.loginValidity) Intent(this, MainActivity::class.java) else Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}
