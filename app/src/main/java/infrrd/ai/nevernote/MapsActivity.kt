package infrrd.ai.nevernote

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import android.widget.Toast



class MapsActivity() : BaseActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        addMarkerForMap(mMap, 34.4343, 45.434, "Marker in sydney", "This is sydney")
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(34.4343, 45.434)))
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
}
