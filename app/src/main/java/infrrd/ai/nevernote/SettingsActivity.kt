package infrrd.ai.nevernote

import android.os.Bundle
import android.app.Activity
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_settings.settings_account_info
import kotlinx.android.synthetic.main.activity_settings.settings_notifications
import kotlinx.android.synthetic.main.activity_settings.settings_camera
import kotlinx.android.synthetic.main.activity_settings.settings_notes
import kotlinx.android.synthetic.main.activity_settings.settings_back_button_icon

class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settings_account_info.setOnClickListener{
            Toast.makeText(this, "account info button clicked", Toast.LENGTH_SHORT).show()
        }

        settings_notifications.setOnClickListener {
            Toast.makeText(this, "notification button clicked", Toast.LENGTH_SHORT).show()
        }

        settings_camera.setOnClickListener{
            Toast.makeText(this, "camera button clicked", Toast.LENGTH_SHORT).show()
        }

        settings_notes.setOnClickListener{
            Toast.makeText(this, "notes button clicked", Toast.LENGTH_SHORT).show()
        }

        settings_back_button_icon.setOnClickListener{
            finish()
        }
    }

}
