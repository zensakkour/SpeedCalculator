package com.example.speedy

class KalmanFilter {
    private var processNoise = 0.1 // Process noise
    private var measurementNoise = 1.0 // Measurement noise
    private var estimate = 0.0 // Initial estimate
    private var errorCovariance = 1.0 // Initial error covariance

    fun filter(measurement: Double): Double {
        val kalmanGain = errorCovariance / (errorCovariance + measurementNoise)
        estimate = estimate + kalmanGain * (measurement - estimate)
        errorCovariance = (1 - kalmanGain) * errorCovariance + processNoise

        // Dynamically adjust noise parameters based on the measurement (e.g., raw speed)
        processNoise = if (measurement > 50) 0.05 else 0.1  // Lower process noise at higher speeds
        measurementNoise = if (measurement > 50) 0.8 else 1.0 // Adjust measurement noise similarly

        return estimate
    }
}
