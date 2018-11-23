package mci.fapra.fapra_imu;

import android.content.Context;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;



public class TouchActivity extends AppCompatActivity implements TouchFragment.ExchangeTouchFragment,
        FittsFragment.ExchangeFittsFragment {

    private final String TAG = this.getClass().getSimpleName();
    private SensorManager sm;
    private SensorWriter sw;

    private TouchFragment touchFragment;
    private FittsFragment fittsFragment;

    private int iteration = 0;
    private int pID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        hideSystemUI();

        pID = getIntent().getIntExtra("PARTICIPANT_ID", -1);

        if(savedInstanceState == null){
            touchFragment = TouchFragment.newInstance(this, pID);
            fittsFragment = FittsFragment.newInstance(this, pID);
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
        } else{
            ft.add(R.id.fragment_placeholder, touchFragment, "touch");
        }

        if (fittsFragment.isAdded()) { ft.replace(R.id.fragment_placeholder, touchFragment); }
        ft.commit();
    }

    private void displayFittsFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fittsFragment.isAdded()){
            ft.show(fittsFragment);
        } else {
            ft.add(R.id.fragment_placeholder, fittsFragment, "fitts");
        }

        if (touchFragment.isAdded()) { ft.replace(R.id.fragment_placeholder, fittsFragment); }
        ft.commit();
    }


    public void finishTask() {
        sw.onStop();
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

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();
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
