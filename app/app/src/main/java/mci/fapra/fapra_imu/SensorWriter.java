package mci.fapra.fapra_imu;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorWriter {

    private final String TAG = Writer.class.getName();
    private final Sensor gyrometer;
    private final Sensor accelerometer;
    public SensorEventListener SensorListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            String sensorName = event.sensor.getName();
            Log.d(TAG, sensorName + System.currentTimeMillis() + ": \t\tX: " + event.values[0] + ";\t\tY: " + event.values[1] + ";\t\tZ: " + event.values[2]);
        }
    };
    SensorManager sensorManager;
    private Writer w;

    public SensorWriter(int pID, SensorManager sensorManager) {
        w = new Writer("" + pID, true);

        this.sensorManager = sensorManager;
        gyrometer = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    public void update() {
        long time = System.currentTimeMillis();
        //w.writeSensor(time, );
    }

    public void onResume() {
        sensorManager.registerListener(SensorListener, gyrometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(SensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        sensorManager.unregisterListener(SensorListener);
    }
}
