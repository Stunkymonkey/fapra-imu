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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;


public class TouchActivity extends AppCompatActivity {


    private final String TAG = Writer.class.getName();

    private SensorManager sm;
    private SensorWriter sw;
    private int iteration = 0;
    private Point[] conditions;
    private int pID;
    private ImageView circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        hideSystemUI();

        pID = getIntent().getIntExtra("PARTICIPANT_ID",-1);

        conditions = this.createConditions();
        circle = findViewById(R.id.circle);

        sm = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sw = new SensorWriter(pID, sm);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        startTask();
    }


    private void startTask(){
        if (iteration<Constants.AMOUNT_REPETITIONS-1) {
            final Point p = conditions[iteration];
            circle.setX(p.x);
            circle.setY(p.y);
            circle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Runde: " + iteration + "/" + Constants.AMOUNT_REPETITIONS, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "" + p.x + "|" + p.y);
                    nextTask();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Done! ", Toast.LENGTH_SHORT).show();
        }
    }

    public void nextTask(){
        iteration++;
        startTask();
    }

    public void finishTask(){
        //TODO implement
    }
    // TODO new function name
    public void end() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        showSystemUI();
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

    /**
     * Create a list with random
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
