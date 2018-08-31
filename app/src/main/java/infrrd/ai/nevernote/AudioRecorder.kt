package infrrd.ai.nevernote

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageButton
import kotlinx.android.synthetic.main.audio_recorder.*
import java.io.IOException

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class AudioRecorder : AppCompatActivity() {

    private var fileName: String = ""

    private var mediaRecorder: MediaRecorder? = null

    private var mediaPlayer: MediaPlayer? = null

    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    private val LOG_TAG = "AudioRecordTest"

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) finish()
    }


    private fun onRecord(start: Boolean) = if (start) {
        startRecording()
    } else {
        stopRecording()
    }

    private fun onPlay(start: Boolean) = if (start) {
        startPlaying()
    } else {
        pausePlaying()
    }

    private fun startPlaying() {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
                this?.setOnCompletionListener {
                    Log.d("lookie","inside done")
                    play_pause.performClick()
                }
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }
        }

    }

    private fun stopPlaying() {
        mediaPlayer?.release()
        mediaPlayer = null
        player.visibility = View.GONE
    }

    private fun pausePlaying(){
        mediaPlayer?.pause()
    }

    private fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            try {
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }

            start()
        }
        player.visibility = View.GONE
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        player.visibility = View.VISIBLE
        mediaRecorder = null
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        fileName = "${externalCacheDir.absolutePath}/audiorecordtest.3gp"

        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        setContentView(R.layout.audio_recorder)
        recorder.setOnClickListener(object : View.OnClickListener {
            var startRecording = true
            override fun onClick(view: View) {
                onRecord(startRecording)
                if(startRecording) {
                    (view as ImageButton).setImageResource(R.drawable.stop_icon)
                }
                else {
                    (view as ImageButton).setImageResource(R.drawable.mic_icon)
                }

                startRecording = !startRecording
            }
    })

        play_pause.setOnClickListener(object : View.OnClickListener {
            var startPlaying = true
            override fun onClick(view: View) {

                if(startPlaying) {
                    onPlay(startPlaying)
                    (view as ImageButton).setImageResource(R.drawable.pause_icon)
                }
                else {
                    onPlay(startPlaying)
                    (view as ImageButton).setImageResource(R.drawable.play_icon)
                }
                startPlaying = !startPlaying
            }
        })

        playerStop.setOnClickListener {
            stopPlaying()
        }
    }
}


