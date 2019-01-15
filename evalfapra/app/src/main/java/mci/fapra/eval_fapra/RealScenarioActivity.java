package mci.fapra.eval_fapra;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import static java.lang.Thread.sleep;

public class RealScenarioActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    private ImageView pointer;
    private Predict pred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scenario1);
        pointer = findViewById(R.id.pointer);

        final Context tmp_context = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                pred = new Predict(tmp_context);
                predictPoint();
            }
        }).start();
        hideSystemUI();
    }

    private void predictPoint(){
        while(true){
            try {
                sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            float[] predictedPoints = pred.predict();
            Log.w(TAG, "" + (int) predictedPoints[0] +  ","  + (int) predictedPoints[1]);
            moveView(pointer, (int) predictedPoints[0], (int) predictedPoints[1]);
        }
    }

    private void moveView(View v, int x, int y) {
        v.setX(x);
        v.setY(y);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @SuppressLint("InlinedApi")
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
