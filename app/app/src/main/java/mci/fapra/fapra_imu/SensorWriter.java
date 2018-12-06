package mci.fapra.fapra_imu;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.List;

public class SensorWriter {

    private final String TAG = this.getClass().getSimpleName();
    private final Sensor gyrometer;
    private final Sensor accelerometer;
    private final Sensor accelerometer_lin;
    private final Sensor magnetometer;
    private final Sensor gravity;
    private final Sensor rotation;
    private final Writer accWriter;
    private final Writer gyroWriter;
    private final Writer oriWriter;
    private final Writer magnetoWriter;
    private final Writer gravWriter;
    private final Writer rotWriter;
    SensorManager sensorManager;
    private boolean[] sensorReady = new boolean[6];
    public SensorEventListener AccLinListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (!sensorReady[0]) {
                sensorReady[0] = true;
            }
            accWriter.writeSensor(System.currentTimeMillis(), event.values[0], event.values[1], event.values[2]);
            // Log.d(TAG, event.sensor.getName() + System.currentTimeMillis() + ": \t\tX: " + event.values[0] + ";\t\tY: " + event.values[1] + ";\t\tZ: " + event.values[2]);
        }
    };
    public SensorEventListener GyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (!sensorReady[1]) {
                sensorReady[1] = true;
            }
            gyroWriter.writeSensor(System.currentTimeMillis(), event.values[0], event.values[1], event.values[2]);
            // Log.d(TAG, event.sensor.getName() + System.currentTimeMillis() + ": \t\tX: " + event.values[0] + ";\t\tY: " + event.values[1] + ";\t\tZ: " + event.values[2]);
        }
    };
    public SensorEventListener MagnetoListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (!sensorReady[2]) {
                sensorReady[2] = true;
            }
            magnetoWriter.writeSensor(System.currentTimeMillis(), event.values[0], event.values[1], event.values[2]);
            // Log.d(TAG, event.sensor.getName() + System.currentTimeMillis() + ": \t\tX: " + event.values[0] + ";\t\tY: " + event.values[1] + ";\t\tZ: " + event.values[2]);
        }
    };
    public SensorEventListener GravListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (!sensorReady[3]) {
                sensorReady[3] = true;
            }
            gravWriter.writeSensor(System.currentTimeMillis(), event.values[0], event.values[1], event.values[2]);
            // Log.d(TAG, event.sensor.getName() + System.currentTimeMillis() + ": \t\tX: " + event.values[0] + ";\t\tY: " + event.values[1] + ";\t\tZ: " + event.values[2]);
        }
    };
    public SensorEventListener RotListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (!sensorReady[4]) {
                sensorReady[4] = true;
            }
            rotWriter.writeSensor(System.currentTimeMillis(), event.values[0], event.values[1], event.values[2]);
            // Log.d(TAG, event.sensor.getName() + System.currentTimeMillis() + ": \t\tX: " + event.values[0] + ";\t\tY: " + event.values[1] + ";\t\tZ: " + event.values[2]);
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
                    oriWriter.writeSensor(System.currentTimeMillis(), orientation[1], orientation[2], orientation[0]);
                }
            }
        }
    };

    public SensorWriter(int pID, SensorManager sensorManager) {
        String model_name = Constants.getNameForModel();
        accWriter = new Writer("fapra_imu-" + pID + "-acc-" + model_name, true, false);
        gyroWriter = new Writer("fapra_imu-" + pID + "-gyro-" + model_name, true, false);
        oriWriter = new Writer("fapra_imu-" + pID + "-ori-" + model_name, true, false);
        magnetoWriter = new Writer("fapra_imu-" + pID + "-mag-" + model_name, true, false);
        gravWriter = new Writer("fapra_imu-" + pID + "-grav-" + model_name, true, false);
        rotWriter = new Writer("fapra_imu-" + pID + "-rot-" + model_name, true, false);

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
        accWriter.flush();
        gyroWriter.flush();
        oriWriter.flush();
        magnetoWriter.flush();
        gravWriter.flush();
        rotWriter.flush();
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
}
