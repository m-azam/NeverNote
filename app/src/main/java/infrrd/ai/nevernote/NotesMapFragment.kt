package infrrd.ai.nevernote

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map_view.map_view

class NotesMapFragment: Fragment(), OnMapReadyCallback {

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mMapView:MapView
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
        mView = inflater.inflate(R.layout.activity_map_view, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mMapView = map_view
        if (mMapView != null){
            mMapView.onCreate(null)
            mMapView.onResume()
            mMapView.getMapAsync(this)
        }
    }




    override fun onMapReady(googleMap: GoogleMap?) {
        MapsInitializer.initialize(context)
        mGoogleMap = googleMap!!
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap.addMarker(MarkerOptions().position(LatLng(34.35345, 45.36535)).title("title").snippet("hope to be there shoon"))

        val cameraPosition = CameraPosition.builder().target(LatLng(34.3535, 35.3635)).zoom(12.toFloat()).bearing(34.3434.toFloat()).tilt(34.3534.toFloat()).build()

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))


    }
}