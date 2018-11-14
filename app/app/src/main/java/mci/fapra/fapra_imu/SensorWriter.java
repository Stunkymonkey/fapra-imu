package mci.fapra.fapra_imu;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorWriter {

    private final String TAG = Writer.class.getName();
    private final Sensor gyrometer;
    private final Sensor accelerometer;

    private final Writer accWriter;
    private final Writer gyroWriter;
    public SensorEventListener AccListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            accWriter.writeSensor(System.currentTimeMillis(), event.values[0], event.values[1], event.values[2]);
            // Log.d(TAG, event.sensor.getName() + System.currentTimeMillis() + ": \t\tX: " + event.values[0] + ";\t\tY: " + event.values[1] + ";\t\tZ: " + event.values[2]);
        }
    };
    public SensorEventListener GyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            gyroWriter.writeSensor(System.currentTimeMillis(), event.values[0], event.values[1], event.values[2]);
            // Log.d(TAG, event.sensor.getName() + System.currentTimeMillis() + ": \t\tX: " + event.values[0] + ";\t\tY: " + event.values[1] + ";\t\tZ: " + event.values[2]);
        }
    };
    SensorManager sensorManager;

    public SensorWriter(int pID, SensorManager sensorManager) {
        String model_name = Constants.getNameForModel();
        accWriter = new Writer("fapra_imu-" + pID + "-acc-" + model_name, true);
        gyroWriter = new Writer("fapra_imu-" + pID + "-gyro-" + model_name, true);

        this.sensorManager = sensorManager;
        gyrometer = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    public void onResume() {
        sensorManager.registerListener(AccListener, gyrometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(GyroListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        sensorManager.unregisterListener(AccListener);
        sensorManager.unregisterListener(GyroListener);
    }
}
