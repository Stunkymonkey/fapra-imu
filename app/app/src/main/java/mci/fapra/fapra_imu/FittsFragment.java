package mci.fapra.fapra_imu;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Random;

public class FittsFragment extends Fragment{

    // variables for dragging
    private int initialX;
    private int initialY;
    private Point initialWindow;

    ExchangeFittsFragment stt;

    private ImageView tile = null;
    private ImageView target = null;
    static TouchActivity touchActivity;


   //TODO delete this garbage if unused
    public View onCreateView2(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        View myView = inflater.inflate(R.layout.fitts_fragment, container,false);



        touchActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return myView;
    }

    public static FittsFragment newInstance(TouchActivity ta){
        FittsFragment ft = new FittsFragment();
        touchActivity = ta;
        return ft;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            stt = (ExchangeFittsFragment) touchActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException(touchActivity.toString()
                    + " must implement ExchangeFittsFragmentInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fitts_fragment, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v,savedInstanceState);
        tile = v.findViewById(R.id.tile);
        target = v.findViewById(R.id.target);

        if(new Random().nextBoolean()) {
            moveView(tile, (int) (Constants.getScreenWidth() * 0.05), (int) (Constants.getScreenHeight() * 0.66));
            moveView(target, (int) (Constants.getScreenWidth() * 0.70), (int) (Constants.getScreenHeight() * 0.66));
        } else {
            moveView(tile, (int) (Constants.getScreenWidth() * 0.70), (int) (Constants.getScreenHeight() * 0.66));
            moveView(target, (int) (Constants.getScreenWidth() * 0.05), (int) (Constants.getScreenHeight() * 0.66));
        }

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
                            stt.swapToTouch();
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
    public interface ExchangeFittsFragment{
        void swapToTouch();
    }
}
