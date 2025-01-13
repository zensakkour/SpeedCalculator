package com.example.speedy

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.TextView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlin.math.*

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var locationManager: LocationManager
    private lateinit var speedTextView: TextView
    private lateinit var maxSpeedTextView: TextView
    private lateinit var avgSpeedTextView: TextView
    private var previousLocation: Location? = null
    private var kalmanFilter: KalmanFilter = KalmanFilter()
    private var lastSpeed = 0.0  // Variable for low-pass filter
    private val alpha = 0.1 // Smoothing factor for low-pass filter
    private var totalSpeed = 0.0
    private var speedCount = 0
    private var maxSpeed = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        speedTextView = findViewById(R.id.speedTextView)
        maxSpeedTextView = findViewById(R.id.maxSpeedTextView)
        avgSpeedTextView = findViewById(R.id.avgSpeedTextView)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            2,  // Update interval (ms)
            0.05f,   // Minimum distance change (meters)
            this
        )
    }

    override fun onLocationChanged(location: Location) {
        if (previousLocation != null) {
            val distance = haversineDistance(
                previousLocation!!.latitude,
                previousLocation!!.longitude,
                location.latitude,
                location.longitude
            )
            val timeDelta = (location.time - previousLocation!!.time) / 2.0 // in seconds
            if (timeDelta > 0) {
                val rawSpeed = (distance / timeDelta) * 3.6 // Convert m/s to km/h
                val filteredSpeed = kalmanFilter.filter(rawSpeed)
                val smoothedSpeed = lowPassFilter(filteredSpeed)

                // Update max speed
                if (smoothedSpeed > maxSpeed) {
                    maxSpeed = smoothedSpeed
                }

                // Update average speed
                totalSpeed += smoothedSpeed
                speedCount++

                // Display current, max, and avg speeds
                speedTextView.text = String.format("Speed: %.2f km/h", smoothedSpeed)
                maxSpeedTextView.text = String.format("Max Speed: %.2f km/h", maxSpeed)
                avgSpeedTextView.text = String.format("Avg Speed: %.2f km/h", totalSpeed / speedCount)
            }
        }
        previousLocation = location
    }

    private fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371000.0 // Earth radius in meters
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }

    private fun lowPassFilter(currentSpeed: Double): Double {
        lastSpeed = alpha * currentSpeed + (1 - alpha) * lastSpeed
        return lastSpeed
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        }
    }
}
