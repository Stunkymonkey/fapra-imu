package mci.fapra.fapra_imu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class TouchActivity extends AppCompatActivity implements TouchFragment.ExchangeTouchFragment,
        FittsFragment.ExchangeFittsFragment {

    private final String TAG = this.getClass().getSimpleName();
    int clicked_x = 0;
    int clicked_y = 0;
    private SensorManager sm;
    private SensorWriter sw;
    private Writer writer;
    private RelativeLayout touchLayout;

    private TouchFragment touchFragment;
    private FittsFragment fittsFragment;

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

        pID = getIntent().getIntExtra("PARTICIPANT_ID", -1);

        if(savedInstanceState == null){
            touchFragment = TouchFragment.newInstance(this, pID);
            fittsFragment = FittsFragment.newInstance(this);
        }

        sm = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sw = new SensorWriter(pID, sm);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        displayTouchFragment();
    }


    private void displayTouchFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (touchFragment.isAdded()){
            ft.show(touchFragment);
        } else {
            ft.add(R.id.fragment_placeholder, touchFragment, "touch");
        }

        if (fittsFragment.isAdded()) { ft.hide(fittsFragment); }
        ft.commit();
    }

    private void displayFittsFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fittsFragment.isAdded()){
            ft.show(fittsFragment);
        } else {
            ft.add(R.id.fragment_placeholder, fittsFragment, "fitts");
        }

        if (touchFragment.isAdded()) { ft.hide(touchFragment); }
        ft.commit();
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

    @Override
    public void swapToTouch() {
        this.displayTouchFragment();
    }

    @Override
    public void swapToFitts() {
        iteration++;
        if (iteration<Constants.AMOUNT_REPETITIONS-1) {
            this.displayFittsFragment();
        } else {
            finishTask();
        }
    }
}
