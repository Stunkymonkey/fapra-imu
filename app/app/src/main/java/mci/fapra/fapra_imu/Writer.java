package mci.fapra.fapra_imu;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    private final String TAG = Writer.class.getName();
    private FileWriter writer;
    private boolean isSensor;

    public Writer(String filename, boolean isSensor) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), filename + "-0");
        this.isSensor = isSensor;

        int counter = 1;
        while (file.exists()) {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), filename + "-" + counter);
            counter += 1;
        }

        String header = "";
        if (isSensor) {
            header = "time;x-gyro;y-gyro;z-gyro;x-accel;y-accel;z-accel";
        } else {
            header = "time;x-press;y-press;x-circle;y-circle";
        }

        Log.i(TAG, "Opening File");
        try {
            writer = new FileWriter(file, false);
            writer.append(header);
            writer.flush();
        } catch (IOException e) {
            Log.e(TAG, "Error Opening/Writing Path", e);
        }
    }

    public void writeSensor(long time, float x_gyro, float y_gyro, float z_gyro, float x_accel, float y_accel, float z_accel) {
        if (!isSensor) {
            Log.e(TAG, "Error wrong writer");
            return;
        }

        String line = "";
        line += time + ";";
        line += x_gyro + ";";
        line += y_gyro + ";";
        line += z_gyro + ";";
        line += x_accel + ";";
        line += y_accel + ";";
        line += z_accel;

        try {
            writer.append(line);
            writer.flush();
        } catch (IOException e) {
            Log.e(TAG, "Error writing sensor", e);
        }
    }

    public void writeAction(long time, int x_press, int y_press, int x_circle, int y_circle) {
        if (isSensor) {
            Log.e(TAG, "Error wrong writer");
            return;
        }

        String line = "";
        line += time + ";";
        line += x_press + ";";
        line += y_press + ";";
        line += x_circle + ";";
        line += y_circle;

        try {
            writer.append(line);
            writer.flush();
        } catch (IOException e) {
            Log.e(TAG, "Error writing action", e);
        }
    }

    public void close() {
        Log.i(TAG, "Close File");
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Log.e(TAG, "Error Closing File", e);
        }
    }
}
