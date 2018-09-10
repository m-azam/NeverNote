package infrrd.ai.nevernote

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import infrrd.ai.nevernote.objects.AppPreferences
import kotlinx.android.synthetic.main.base_activity.*

abstract class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)
        layoutInflater.inflate(getContentView(), layout_container, true)
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

    }

    abstract fun getContentView(): Int

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort -> {
                Toast.makeText(this, "sort button is clicked", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.settings -> {
                openSettingsActivity()
                true
            }
            R.id.action_search -> {
                toggle.isDrawerIndicatorEnabled = false//Fixes visual glitch when expanding search bar
                true
            }
            R.id.notes_map_view -> {
                openMapViewActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.trash -> {
                Toast.makeText(this, "trash button clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.completed -> {
                Toast.makeText(this, "completed button clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.all_notes -> {
                Toast.makeText(this, "all_notes button clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.sign_out -> {
                AppPreferences.loginValidity = false
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun openSettingsActivity(){
        val settingsActivityIntent = Intent(this, SettingsActivity::class.java)
        startActivity(settingsActivityIntent)
    }

    private fun openMapViewActivity(){
        val mapViewActivityIntent = Intent(this, MapsActivity::class.java)
        startActivity(mapViewActivityIntent)
    }
}