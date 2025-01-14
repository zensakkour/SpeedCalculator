# **Speedy - GPS Speed Calculator**

## **Project Overview**
**Speedy** is an Android app that calculates your current speed using the device's GPS sensor. The app updates the speed in real-time by using advanced methods to ensure accuracy and smoothness. It makes use of the **Haversine formula** for distance calculation and a **Kalman filter** for smoothing the speed readings, reducing noise caused by inaccurate GPS data.

## **Features**
- Real-time speed calculation based on GPS data.
- Uses **Haversine formula** to compute accurate distance over the Earth’s surface.
- Applies a **Kalman filter** to smooth and optimize speed data.
- Simple and user-friendly interface to display speed in km/h.

## **Technologies Used**
- **Kotlin** for Android development.
- **Android Location Manager** for GPS-based location updates.
- **Kalman Filter** for noise reduction in speed calculations.
- **Haversine Formula** for accurate distance measurement.

---

## **How It Works**

### **1. GPS Location Updates**
The app utilizes the **Android LocationManager** to request GPS updates from the device. This allows the app to get the device's **latitude**, **longitude**, and **time** at regular intervals. The LocationManager uses the GPS provider for high accuracy.

### **2. Distance Calculation - Haversine Formula**
To calculate the distance between two geographical points (based on latitude and longitude), we use the **Haversine Formula**. This formula accounts for the Earth's curvature, making it ideal for GPS-based distance calculations.

The Haversine formula is:

$$
d = 2r \times \text{asin} \left( \sqrt{\sin^2 \left( \frac{\Delta \phi}{2} \right) + \cos \phi_1 \times \cos \phi_2 \times \sin^2 \left( \frac{\Delta \lambda}{2} \right)} \right)
$$

Where:
- $d$ is the distance between two points on the surface of a sphere (in meters).
- $r$ is the radius of the Earth (6371000 meters).
- $\phi_1$ and $\phi_2$ are the latitudes of the two points (in radians).
- $\Delta \phi$ is the difference in latitudes.
- $\Delta \lambda$ is the difference in longitudes.

### **3. Speed Calculation**
Once the distance between two consecutive GPS points is calculated using the Haversine formula, the app calculates the **speed** using the formula:

$$
\text{speed} = \frac{\text{distance}}{\text{time}}
$$

Where:
- **Distance** is the distance between the two GPS points (calculated using the Haversine formula).
- **Time** is the difference in the timestamps of the two GPS readings.

To convert the speed from meters per second (m/s) to kilometers per hour (km/h), we multiply by a factor of 3.6:

$$
\text{speed (km/h)} = \text{speed (m/s)} \times 3.6
$$

### **4. Kalman Filter**
GPS speed data is often noisy due to various environmental factors (e.g., signal bounce, interference). To smooth the speed readings, the app applies a **Kalman filter**.

The Kalman filter is a recursive algorithm that uses a series of measurements observed over time, containing statistical noise, and produces estimates of unknown variables that tend to be more accurate than those based on a single measurement alone. It combines the current estimate with the new measurement to correct and refine the estimated speed.

The Kalman filter works through two main steps:
1. **Prediction**: Using the previous estimate and the process model to predict the next state.
2. **Correction**: Adjusting the predicted state based on the new measurement.

---

## **App Structure**

### **1. MainActivity.kt**
- The main entry point of the app. It handles:
  - Requesting GPS location updates.
  - Calculating distance and speed.
  - Applying the Kalman filter to smooth the speed readings.
  - Displaying the speed on the user interface.

### **2. KalmanFilter.kt**
- A class implementing the Kalman filter algorithm to smooth the calculated speed values.

### **3. activity_main.xml**
- Defines the user interface, containing a `TextView` to display the speed in **km/h**.

### **4. AndroidManifest.xml**
- Defines app permissions and declares the `MainActivity` as the main launcher activity.

---

## **Installation & Setup**

1. **Clone the repository**:
   ```bash
   git clone https://github.com/zensakkour/speedy.git
