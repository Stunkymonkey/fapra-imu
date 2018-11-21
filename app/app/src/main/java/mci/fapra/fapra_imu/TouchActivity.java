package mci.fapra.fapra_imu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;


public class TouchActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    int clicked_x = 0;
    int clicked_y = 0;
    private SensorManager sm;
    private SensorWriter sw;
    private Writer writer;
    private FragmentTransaction ft = null;
    private RelativeLayout touchLayout;

    private Toast t;
    private int iteration = 0;
    private Point[] conditions;
    private int pID;
    private ImageView cross;
    private int circleSize = Constants.getTargetPixelsForPhone();
    private boolean was_fitts = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        hideSystemUI();

        touchLayout = findViewById(R.id.touch);
        pID = getIntent().getIntExtra("PARTICIPANT_ID", -1);

        conditions = this.createConditions();
        cross = findViewById(R.id.circle);
        cross.setLayoutParams(new RelativeLayout.LayoutParams(circleSize, circleSize));

        if (ft == null){
            ft = this.getSupportFragmentManager().beginTransaction();
        }
        FittsTask fittsFragment = FittsTask.newInstance(this);
        ft.replace(R.id.fitts_placeholder, fittsFragment);

        sm = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sw = new SensorWriter(pID, sm);
        String model_name = Constants.getNameForModel();
        writer = new Writer("fapra_imu-" + pID + "-points-" + model_name, false);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        startTask();
    }


    private void startTask() {
        if (was_fitts) {
            if (iteration < Constants.AMOUNT_REPETITIONS - 1) {
                final Point p = conditions[iteration];
                cross.setX(p.x);
                cross.setY(p.y);
                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        writer.writeAction(System.currentTimeMillis(), clicked_x, clicked_y, p.x, p.y);
                        showToast("Runde: " + (iteration + 1) + "/" + Constants.AMOUNT_REPETITIONS);
                        Log.d(TAG, "" + p.x + "|" + p.y);
                        iteration++;
                        touchLayout.removeView(cross);
                        //startTask();
                        ft.commit();

                    }
                });
                cross.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            clicked_x = (int) event.getRawX();
                            clicked_y = (int) event.getRawY();
                        }
                        return false;
                    }
                });
            } else {
                showToast("Done!");
                finishTask();
            }
        } else {


        }
        was_fitts = !was_fitts;


    }

    void showToast(String text) {
        if (t != null) {
            t.cancel();
        }
        t = Toast.makeText(this, text, Toast.LENGTH_LONG);
        t.show();

    }

    public void finishTask() {
        sw.onStop();
        writer.close();
        finish();
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
     *
     * @return list holding random points for the creation of the circles
     */
    private Point[] createConditions() {
        Point[] conditions = new Point[Constants.AMOUNT_REPETITIONS];
        Random r = new Random();

        for (int i = 0; i < Constants.AMOUNT_REPETITIONS; i++) {
            int x = r.nextInt(Constants.getScreenWidth() - circleSize);
            int y = r.nextInt(Constants.getScreenHeight() - circleSize);
            conditions[i] = new Point(x, y);
        }
        return conditions;
    }

    private void startFitts() {
        Intent intent = new Intent(this, FittsTask.class);
        startActivity(intent);
    }
}
