package mci.fapra.fapra_imu;

import android.graphics.Point;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.Touch;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class FittsTask extends Fragment{

    // variables for dragging
    private int initialX;
    private int initialY;
    private Point initialWindow;

    private ImageView tile = null;
    private ImageView target = null;
    static TouchActivity touchActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        View myView = inflater.inflate(R.layout.activity_fitts_task, container,false);
        tile = myView.findViewById(R.id.tile);
        target = myView.findViewById(R.id.target);

        moveView(tile, (int)(Constants.getScreenWidth()*0.05),(int)(Constants.getScreenHeight()*0.66));
        moveView(target, (int)(Constants.getScreenWidth()*0.70),(int)(Constants.getScreenHeight()*0.66));
        return myView;
    }
    /**
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitts_task);
        hideSystemUI();



        moveView(tile, (int)(Constants.getScreenWidth()*0.05),(int)(Constants.getScreenHeight()*0.66));
        moveView(target, (int)(Constants.getScreenWidth()*0.70),(int)(Constants.getScreenHeight()*0.66));

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        startTask();
    }
    **/
    public static FittsTask newInstance(TouchActivity ta){
        FittsTask ft = new FittsTask();
        touchActivity = ta;
        return ft;
    }

    protected void startTask(){

        tile.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction() & MotionEvent.ACTION_MASK;

            int x = (int) event.getRawX();
            int y = (int) event.getRawY();

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:

                    initialX = x;
                    initialY = y;

                    initialWindow = getViewPos(v);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:

                    Point t = getViewPos(target);
                    Point s = getViewPos(tile);
                    double dist = Math.sqrt(Math.pow(t.x - s.x, 2) + Math.pow(t.y - s.y, 2));

                    if (dist < Constants.MATCH_THRESHOLD_PX) {
                        //Toast.makeText(getApplicationContext(), "Larry",Toast.LENGTH_SHORT).show();
                        //finish();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:

                    int deltaTouchX = initialX - x;
                    int deltaTouchY = initialY - y;

                    if (initialWindow != null) {
                        moveView(v, initialWindow.x - deltaTouchX, initialWindow.y - deltaTouchY);
                    }

                    break;
            }
            return true;
        }
    });
    }

    private Point getViewPos(View v){
        return new Point((int)v.getX(),(int)v.getY());
    }

    private void moveView(View v, int x, int y){
        v.setX(x);
        v.setY(y);
    }
    
    /**
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }**/

}
