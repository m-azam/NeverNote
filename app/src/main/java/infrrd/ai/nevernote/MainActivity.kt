package infrrd.ai.nevernote

import android.os.Bundle
import android.support.v4.widget.DrawerLayout

class MainActivity : BaseActivity() {
    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
