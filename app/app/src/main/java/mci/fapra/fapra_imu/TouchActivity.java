package mci.fapra.fapra_imu;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.Random;

import mci.fapra.fapra_imu.Constants;

public class TouchActivity extends AppCompatActivity {


    private final String TAG = Writer.class.getName();

    private SensorManager sm;
    private SensorWriter sw;
    private int iteration = 0;
    private Point[] conditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        hideSystemUI();
        conditions = this.createConditions();


        sm = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sw = new SensorWriter(-1, sm);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    // TODO new function name
    public void end() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        showSystemUI();
    }

    public void nextTask(){
        iteration++;
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void onResume() {
        super.onResume();
        sw.onResume();
    }

    public void onStop() {
        super.onStop();
        sw.onStop();
    }

    private void moveView(View v, int x, int y){
        RelativeLayout.LayoutParams params;
        params = (RelativeLayout.LayoutParams) v.getLayoutParams();
        params.leftMargin = x;
        params.topMargin = y;
        v.setLayoutParams(params);
    }

    /**
     *
     * @return list holding random points for the creation of the circles
     */
    private Point[] createConditions(){
        Point[] conditions = new Point[Constants.AMOUNT_REPETITIONS];

        int x_offset = 0;
        int y_offset = 0;

        for (int i = 0; i < Constants.AMOUNT_REPETITIONS; i++) {
            int x = new Random().nextInt(Constants.SCREEN_WIDTH - Constants.getTargetPixelsForPhone());
            int y = new Random().nextInt(Constants.getScreenHeight() - y_offset) + y_offset - Constants.getTargetPixelsForPhone();
            conditions[i] = new Point(x, y);
        }
        return conditions;
    }
}
