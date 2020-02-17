package com.milo.birdapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_mark_bird_location.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MarkBirdLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null

    // Bird marker vars
    //private lateinit var latitude: String
    //private lateinit var longitude: String
    private var newLatLng: LatLng? = null
    private var address: String = ""

    fun centerMapOnLocation(
        location: Location?,
        title: String?
    ) {

        if (intent.getStringExtra("latLng").isNullOrEmpty()) {
            if (location != null) {
                Log.i("USERLOCATION", LatLng(location.latitude, location.longitude).toString())
                val userLocation = LatLng(location.latitude, location.longitude)
                mMap.addMarker(MarkerOptions().position(userLocation).title(title))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 25f))
            }
        } else {
            val latLngArray = intent.getStringExtra("latLng").split(" ", ",")

            Log.i("LATLNG", latLngArray.toString())
            Log.i("LATLNG", latLngArray[1].substring(1, latLngArray[1].length))

            Log.i("LATLNG", latLngArray[2].substring(0, latLngArray[2].length - 1))

            val latitude = latLngArray[1].substring(1, latLngArray[1].length).toDouble()
            val longitude = latLngArray[2].substring(0, latLngArray[2].length - 1).toDouble()

            val location = LatLng(latitude, longitude)
            val address = intent.getStringExtra("address")

            Toast.makeText(this, location.toString(), Toast.LENGTH_SHORT).show()

            mMap.addMarker(
                MarkerOptions().position(location).title(address)
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 25f))

            /*val location = LatLng(latLng.latitude, latLng.longitude)
            mMap.addMarker(
                MarkerOptions().position(location).title(address)
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 25f))*/
        }
        /*
        if (location != null) {
            Log.i("USERLOCATION", LatLng(location.latitude, location.longitude).toString())
            val userLocation = LatLng(location.latitude, location.longitude)
            mMap.addMarker(MarkerOptions().position(userLocation).title(title))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 25f))
        }*/
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0f,
                        locationListener
                    )
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {

            if (newLatLng != null) {
                val resultIntent = Intent(this, DetailsActivity::class.java)

                //resultIntent.putExtra("latitude", latitude)
                //resultIntent.putExtra("longitude", longitude)
                resultIntent.putExtra("latLng", newLatLng.toString())
                resultIntent.putExtra("address", address)

                Log.i("LATLNG", "onOptionsItemSelected: " + address)
                Log.i("LATLNG", "onOptionsItemSelected: " + newLatLng.toString())
                setResult(RESULT_OK, resultIntent)
            }
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark_bird_location)

        // Setting up Toolbar
        setSupportActionBar(toolBar)

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->

            var geocoder = Geocoder(applicationContext, Locale.getDefault())
            address = ""

            try {
                var listAddresses: List<Address> =
                    geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

                if (listAddresses != null && listAddresses.isNotEmpty()) {
                    if (listAddresses[0].thoroughfare != null) {
                        if (listAddresses[0].subThoroughfare != null) {
                            address += listAddresses[0].subThoroughfare + " "
                        }

                        address += listAddresses[0].thoroughfare
                    }
                }
                //       var sharedPreferences = getSharedPreferences()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (address.equals("")) {
                var sdf = SimpleDateFormat(" HH:mm dd-MM-yyyy")
                address += sdf.format(Date())
            }

            map.clear()
            map.addMarker(MarkerOptions().position(latLng).title(address))
            newLatLng = latLng

            Log.i("LATLNG", newLatLng.toString())
            Log.i("LATLNG", address)

/*
            val resultIntent = Intent(this, DetailsActivity::class.java)
*/

            //latitude = latLng.latitude.toString()
            //longitude = latLng.longitude.toString()


/*            resultIntent.putExtra("latitude", latLng.latitude.toString())
            resultIntent.putExtra("longitude", latLng.longitude.toString())
            resultIntent.putExtra("address", address)

            Log.i("LAT", latLng.latitude.toString())
            Log.i("LONG", latLng.longitude.toString())
            Log.i("ADDRESS", address)
            setResult(RESULT_OK, resultIntent)

            finish()*/
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Zoom in on user location
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener =
            object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    centerMapOnLocation(location, "Your location.")
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String?) {}
                override fun onProviderDisabled(provider: String?) {}
            }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0, 10f, locationListener
            )
            val lastKnownLocation =
                locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            centerMapOnLocation(lastKnownLocation, "Your Location")
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
        setMapLongClick(mMap)
    }
}
