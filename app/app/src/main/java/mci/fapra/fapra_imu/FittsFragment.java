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
    private int pID;
    private Point initialWindow;

    ExchangeFittsFragment stt;

    private Writer writer;
    private ImageView tile = null;
    private ImageView target = null;
    static TouchActivity touchActivity;


    public static FittsFragment newInstance(TouchActivity activity, int pID){
        FittsFragment ft = new FittsFragment();
        touchActivity = activity;
        Bundle args = new Bundle();
        args.putInt("pID",pID);
        ft.setArguments(args);
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
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        pID = getArguments().getInt("pID",-1);
        writer = new Writer("fapra_imu-" + pID + "-fitts-" + Constants.getNameForModel(), false, true);
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
                        //calculate euclidean distance between tile (top left x,y) and target (top left x,y)
                        double dist = Math.sqrt(Math.pow(t.x - s.x, 2) + Math.pow(t.y - s.y, 2));

                        if (dist < Constants.MATCH_THRESHOLD_PX) {
                            writer.writeFitts(System.currentTimeMillis());
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

    @Override
    public void onDetach() {
        super.onDetach();
        writer.close();
    }

    private Point getViewPos(View v){
        return new Point((int)v.getX(),(int)v.getY());
    }

    private void moveView(View v, int x, int y){
        v.setX(x);
        v.setY(y);
    }

    public interface ExchangeFittsFragment{
        void swapToTouch();
    }
}
