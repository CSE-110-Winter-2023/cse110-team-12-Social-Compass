package edu.ucsd.cse110.cse110_project;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class OrientationService implements SensorEventListener {
    private static OrientationService instance;

    private final SensorManager sensorManager;
    private float[] accelerometerReading;
    private float[] magnetometerReading;
    private MutableLiveData<Float> azimuth;

    /**
     * Constructor for OrientationService
     * @param activity Context needed to initiate SensorManager
     */
    private OrientationService(Activity activity) {
        this.azimuth = new MutableLiveData<>();
        this.sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        // Register sensor listeners
        this.registerSensorListeners();
    }

    private void registerSensorListeners(){
        // Register our listener to the accelerometer and magnetometer
        // We need both pieces of data to compute the orientation
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public static OrientationService singleton(Activity activity){
        if(instance == null) {
            instance = new OrientationService(activity);
        }
        return instance;
    }

    /**
     * This method is called when the sensor detects a change in value.
     * @param event the event containing the values we need
     */
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //If we only have this sensor, we can't compute the orientation with it alone.
            // But we should still save it for later
            accelerometerReading = event.values;
        }
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // If we only have this sensor, we can't compute the orientation with it alone.
            // But we should still save it for later.
            magnetometerReading = event.values;
        }
        if(accelerometerReading != null && magnetometerReading != null){
            // we have both sensors, so we can compute orientation
            onBothSensorDataAvailable();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    /**
     * Called when we have readings for both sensors.
     */
    private void onBothSensorDataAvailable() {
        // Discount contract checking. Think Design by Contract!
        if (accelerometerReading == null || magnetometerReading == null) {
            throw new IllegalStateException("Both sensors must be available to compute orientation.");
        }

        var r = new float[9];
        var i = new float[9];
        //Now we do some linear algebra magic using the two sensor readings.
        var success = SensorManager.getRotationMatrix(r, i, accelerometerReading, magnetometerReading);
        // Did it work?
        if (success){
            // Ok we're good to go!
            var orientation = new float[3];
            SensorManager.getOrientation(r, orientation);

            this.azimuth.postValue(orientation[0]);
        }
    }

    public void unregisterSensorListeners() {sensorManager.unregisterListener(this);}

    public LiveData<Float> getOrientation(){return this.azimuth;}

    public void setMockOrientationSource(MutableLiveData<Float> mockDataSource){
        unregisterSensorListeners();
        this.azimuth = mockDataSource;
    }
}
