package mci.fapra.eval_fapra;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;

public class Predict {
    private final String TAG = this.getClass().getSimpleName();

    private TensorFlowRegression tfr;
    private Sensors sensors;
    private float[][] data;

    public Predict(Context c) {
        tfr = new TensorFlowRegression(c);
        SensorManager sm = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
        sensors = new Sensors(sm);
        data = new float[Constants.WINDOW_SIZE][Constants.SENSORS];
        while (!sensors.allSensorsactive()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "WAITING");
        }
    }

    public float[] predict() {
        float[] current = sensors.getSensorData();
        for (int i = 0; i < data.length - 1; i++) {
            System.arraycopy(data[i + 1], 0, data[i], 0, data[i].length);
        }
        System.arraycopy(current, 0, data[data.length - 1], 0, current.length);

        float[] tmp = new float[Constants.WINDOW_SIZE * Constants.SENSORS];
        int counter = 0;
        for (int i = 0; i < Constants.WINDOW_SIZE; i++) {
            for (int j = 0; j < Constants.SENSORS; j++) {
                tmp[counter] = data[i][j];
                counter ++;
            }
        }
        return tfr.predict(tmp);
    }
}
