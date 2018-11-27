package mci.fapra.fapra_imu;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;


public class TouchActivity extends AppCompatActivity implements TouchFragment.ExchangeTouchFragment,
        FittsFragment.ExchangeFittsFragment {

    private final String TAG = this.getClass().getSimpleName();
    public Writer touchWriter = null;
    public Writer fittsWriter = null;
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

        touchWriter = new Writer("fapra_imu-" + pID + "-points-" + Constants.getNameForModel(), false, false);
        fittsWriter = new Writer("fapra_imu-" + pID + "-fitts-" + Constants.getNameForModel(), false, true);

        if (savedInstanceState == null) {
            touchFragment = TouchFragment.newInstance(this);
            fittsFragment = FittsFragment.newInstance(this);
        }
        sm = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sw = new SensorWriter(pID, sm);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        displayTouchFragment();
    }


    private void displayTouchFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (touchFragment.isAdded()) {
            ft.show(touchFragment);
        } else {
            ft.add(R.id.fragment_placeholder, touchFragment, "touch");
        }

        if (fittsFragment.isAdded()) {
            ft.replace(R.id.fragment_placeholder, touchFragment);
        }
        ft.commit();
    }

    private void displayFittsFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fittsFragment.isAdded()) {
            ft.show(fittsFragment);
        } else {
            ft.add(R.id.fragment_placeholder, fittsFragment, "fitts");
        }

        if (touchFragment.isAdded()) {
            ft.replace(R.id.fragment_placeholder, fittsFragment);
        }
        ft.commit();
    }

    /**
     * Close all writers, go to MainActivity
     */
    public void finishTask() {
        sw.onStop();
        fittsWriter.close();
        touchWriter.close();
        finish();
    }

    // TODO new function name
    public void end() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        showSystemUI();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder finishedDialog = new AlertDialog.Builder(this);
        finishedDialog.setMessage("Do you really want to leave the application?");
        finishedDialog.setTitle("Alert");
        finishedDialog.setCancelable(false);
        finishedDialog.setPositiveButton("LEAVE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishTask();
                    }
                });
        finishedDialog.setNegativeButton("STAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hideSystemUI();
            }
        });
        finishedDialog.show();
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
        if (iteration <= Constants.AMOUNT_REPETITIONS - 1) {
            this.displayFittsFragment();
        } else {
            finishTask();
        }
    }
}
