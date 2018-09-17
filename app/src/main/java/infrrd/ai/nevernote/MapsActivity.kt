package infrrd.ai.nevernote

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.Menu
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import android.widget.Toast
import infrrd.ai.nevernote.objects.Trash
import java.text.SimpleDateFormat
import java.util.ArrayList


class MapsActivity() : BaseActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private val notes: MutableList<Note> = ArrayList()
    private val MY_PERMISSIONS_REQUEST_LOCATION = 1
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap
    var formatter = SimpleDateFormat("dd-MMMM-yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            permission is not granted
//            should we show an explanation
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
//                should show an explanation
                createExplanationDialog()
            }
            else {
//                No explanation needed, we can request the permission
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
        else {
//            permssion has already been granted
            requestMap()
        }
    }



    fun requestPermission(permission:String){
        ActivityCompat.requestPermissions(this, arrayOf(permission), MY_PERMISSIONS_REQUEST_LOCATION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode){
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
//                    permission was granted
                    requestMap()
                } else {
//                    permission denied
                    finish()
                }
                return
            }
            else -> { }
        }
    }

    fun requestMap(){
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnInfoWindowClickListener(this)
        // Add marker and move the camera to the first postion stored in the notes adapter
        notes.add(Note("Task1",
                "Body1","7-JUNE-2013", false, 23.23, 34.23))
        for (notes in notes){
            notes.latitude?.let { lat ->
                notes.longitude?.let { lon ->
                    addMarkerForMap(mMap, lat, lon, notes.title, notes.body)
                }
            }
        }
        notes.first().latitude?.let { lat ->
            notes.first().longitude?.let { lon ->
                mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, lon)))
            }
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_maps
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if(menu != null) menu.findItem(R.id.notes_map_view).setVisible(false)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onInfoWindowClick(marker: Marker) {
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show()
    }

    private fun addMarkerForMap(mMap:GoogleMap, latitude:Double, longitude:Double, title:String, snippet:String){
        mMap.addMarker(MarkerOptions().position(LatLng(latitude, longitude)).title(title).snippet(snippet))
    }

    private fun createExplanationDialog(){
        val explanationDialogBuider = AlertDialog.Builder(this)
        explanationDialogBuider.setMessage("Location permission is required for Maps")
        explanationDialogBuider.setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, id ->
            // User clicked OK button
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            requestMap()
        })
        explanationDialogBuider.setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, id ->
            // User cancelled the dialog
            finish()
        })
        val explanationdialog = explanationDialogBuider.create()
        explanationdialog.show()
    }
}
