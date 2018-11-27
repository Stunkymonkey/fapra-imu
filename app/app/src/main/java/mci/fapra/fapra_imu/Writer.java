package mci.fapra.fapra_imu;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    private final String TAG = this.getClass().getSimpleName();
    private FileWriter writer;
    private boolean isSensor;

    public Writer(String filename, boolean isSensor, boolean isFitts) {
        this.isSensor = isSensor;

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), filename + "-0.csv");


        int counter = 1;
        while (file.exists()) {
            Log.d(TAG, "Creating a new file.");
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), filename + "-" + counter + ".csv");
            counter += 1;
        }

        String header = "";
        if (isSensor) {
            header = "time;x;y;z\n";
        } else {
            header = "time;x-press;y-press;x-circle;y-circle\n";
        }

        if (isFitts) {
            header = "time\n";
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

    public void writeSensor(long time, float x, float y, float z) {
        if (!isSensor) {
            Log.e(TAG, "Error wrong writer");
            return;
        }

        String line = "";
        line += time + ";";
        line += x + ";";
        line += y + ";";
        line += z + "\n";

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
        line += y_circle + "\n";

        try {
            writer.append(line);
            writer.flush();
        } catch (IOException e) {
            Log.e(TAG, "Error writing action", e);
        }
    }

    public void writeFitts(long time) {
        if (isSensor) {
            Log.e(TAG, "Error wrong writer");
            return;
        }
        try {
            writer.append(time + "\n");
            writer.flush();
        } catch (IOException e) {
            Log.e(TAG, "Error writing fitts", e);
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
