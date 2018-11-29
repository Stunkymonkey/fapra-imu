package mci.fapra.fapra_imu;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class FittsFragment extends Fragment {

    static TouchActivity touchActivity;
    ExchangeFittsFragment stt;
    // variables for dragging
    private int iteration = 0;
    private int initialX;
    private int initialY;
    private Point initialWindow;
    private ProgressBar progress;
    private ImageView tile = null;
    private ImageView target = null;
    private TextView fittsText = null;
    private int rectSize = Constants.getTargetPixelsForPhone(13);


    public static FittsFragment newInstance(TouchActivity activity) {
        FittsFragment ft = new FittsFragment();
        touchActivity = activity;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fitts_fragment, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        fittsText = v.findViewById(R.id.fitts_text);
        if (iteration > 1) {
            fittsText.setVisibility(View.INVISIBLE);
        }

        //Set uniform size over all phones for target and tile
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(rectSize, rectSize);
        tile = v.findViewById(R.id.tile);
        target = v.findViewById(R.id.target);
        tile.setLayoutParams(params);
        target.setLayoutParams(params);

        //Progressbar for finished rounds
        final float percent = ((float) (iteration + 1) / Constants.AMOUNT_REPETITIONS * 100);
        progress = v.findViewById(R.id.progress_rounds);
        RelativeLayout.LayoutParams paramsProgress = (RelativeLayout.LayoutParams) progress.getLayoutParams();
        paramsProgress.width = Constants.getScreenWidth() / 100 * 85;
        progress.setLayoutParams(paramsProgress);
        progress.post(new Runnable() {
            @Override
            public void run() {
                progress.setProgress((int) percent);
            }
        });

        if (new Random().nextBoolean()) {
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
                            touchActivity.fittsWriter.writeFitts(System.currentTimeMillis());
                            iteration++;
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
    }

    private Point getViewPos(View v) {
        return new Point((int) v.getX(), (int) v.getY());
    }

    private void moveView(View v, int x, int y) {
        v.setX(x);
        v.setY(y);
    }

    public interface ExchangeFittsFragment {
        void swapToTouch();
    }
}
