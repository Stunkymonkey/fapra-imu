package mci.fapra.eval_fapra;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.List;

public class Sensors {

    private final String TAG = this.getClass().getSimpleName();
    private final Sensor gyrometer;
    private final Sensor accelerometer;
    private final Sensor accelerometer_lin;
    private final Sensor magnetometer;
    private final Sensor gravity;
    private final Sensor rotation;
    SensorManager sensorManager;
    private boolean[] sensorReady = new boolean[6];

    private float[] data = new float[Constants.SENSORS];

    public SensorEventListener AccLinListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (!sensorReady[0]) {
                sensorReady[0] = true;
            }
            // Log.d(TAG, event.sensor.getName() + System.currentTimeMillis() + ": \t\tX: " + event.values[0] + ";\t\tY: " + event.values[1] + ";\t\tZ: " + event.values[2]);
            data[0] = event.values[0];
            data[1] = event.values[1];
            data[2] = event.values[2];
        }
    };
    public SensorEventListener GyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (!sensorReady[1]) {
                sensorReady[1] = true;
            }
            // Log.d(TAG, event.sensor.getName() + System.currentTimeMillis() + ": \t\tX: " + event.values[0] + ";\t\tY: " + event.values[1] + ";\t\tZ: " + event.values[2]);
            data[3] = event.values[0];
            data[4] = event.values[1];
            data[5] = event.values[2];
        }
    };
    public SensorEventListener OriListener = new SensorEventListener() {
        float[] mGravity;
        float[] mGeomagnetic;

        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (!sensorReady[5]) {
                sensorReady[5] = true;
            }
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mGravity = event.values;
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                mGeomagnetic = event.values;
            if (mGravity != null && mGeomagnetic != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                if (success) {
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    // Log.d(TAG, "Orientation " + System.currentTimeMillis() + ": \t\tX: " + orientation[1] + ";\t\tY: " + orientation[2] + ";\t\tZ: " + orientation[0]);
                    data[6] = event.values[0];
                    data[7] = event.values[1];
                    data[8] = event.values[2];
                }
            }
        }
    };
    public SensorEventListener GravListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (!sensorReady[3]) {
                sensorReady[3] = true;
            }
            // Log.d(TAG, event.sensor.getName() + System.currentTimeMillis() + ": \t\tX: " + event.values[0] + ";\t\tY: " + event.values[1] + ";\t\tZ: " + event.values[2]);
            data[9] = event.values[0];
            data[10] = event.values[1];
            data[11] = event.values[2];
        }
    };
    public SensorEventListener MagnetoListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (!sensorReady[2]) {
                sensorReady[2] = true;
            }
            // Log.d(TAG, event.sensor.getName() + System.currentTimeMillis() + ": \t\tX: " + event.values[0] + ";\t\tY: " + event.values[1] + ";\t\tZ: " + event.values[2]);
            data[12] = event.values[0];
            data[13] = event.values[1];
            data[14] = event.values[2];
        }
    };
    public SensorEventListener RotListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (!sensorReady[4]) {
                sensorReady[4] = true;
            }
            // Log.d(TAG, event.sensor.getName() + System.currentTimeMillis() + ": \t\tX: " + event.values[0] + ";\t\tY: " + event.values[1] + ";\t\tZ: " + event.values[2]);
            data[15] = event.values[0];
            data[16] = event.values[1];
            data[17] = event.values[2];
        }
    };

    public Sensors(SensorManager sensorManager) {


        this.sensorManager = sensorManager;
        // dont know why this helps on s3 mini
        List<Sensor> list = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : list) {
            Log.d("List-Sensor", sensor.toString());
        }

        gyrometer = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer_lin = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        rotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        onResume();
    }

    public void onResume() {
        sensorManager.registerListener(AccLinListener, accelerometer_lin, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(GyroListener, gyrometer, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(OriListener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(OriListener, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(MagnetoListener, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(GravListener, gravity, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(RotListener, rotation, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void onStop() {
        sensorManager.unregisterListener(AccLinListener);
        sensorManager.unregisterListener(GyroListener);
        sensorManager.unregisterListener(OriListener);
        sensorManager.unregisterListener(MagnetoListener);
        sensorManager.unregisterListener(GravListener);
        sensorManager.unregisterListener(RotListener);
    }

    public boolean allSensorsactive() {
        for (boolean b : sensorReady) if (!b) return false;
        return true;
    }

    public float[] getSensorData() {
        return data;
    }
}

