package mci.fapra.fapra_imu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.Touch;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class TouchFragment extends Fragment {

    private ImageView cross;
    private Toast t;

    // TODO not sure if used
    private SensorManager sm;
    private SensorWriter sw;

    private Writer writer;
    static TouchActivity touchActivity;

    ExchangeTouchFragment stf;

    private Point[] conditions;

    private int pID;
    private int iteration = 0;
    private int clicked_x = 0;
    private int clicked_y = 0;
    private int crossSize = Constants.getTargetPixelsForPhone();


    public static TouchFragment newInstance(TouchActivity activity, int pID){
        TouchFragment tf = new TouchFragment();
        touchActivity = activity;
        Bundle args = new Bundle();
        args.putInt("pID",pID);
        tf.setArguments(args);
        return tf;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            stf = (ExchangeTouchFragment) touchActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException(touchActivity.toString()
                    + " must implement ExchangeTouchFragmentInterface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        conditions = createConditions();
        pID = getArguments().getInt("pID",-1);
        writer = new Writer("fapra_imu-" + pID + "-points-" + Constants.getNameForModel(), false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.touch_fragment, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
        super.onViewCreated(v,savedInstanceState);
        cross = v.findViewById(R.id.cross);
        cross.setLayoutParams(new RelativeLayout.LayoutParams(crossSize, crossSize));
        final Point p = conditions[iteration];
        Log.d("POINT", "Iteration: "+iteration+p.x+"|"+p.y);
        cross.setX(p.x);
        cross.setY(p.y);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writer.writeAction(System.currentTimeMillis(), clicked_x, clicked_y, p.x, p.y);
                showToast("Round: " + (iteration + 1) + "/" + Constants.AMOUNT_REPETITIONS);
                //Log.d(TAG, "" + p.x + "|" + p.y);
                iteration++;
                //startTask();
                stf.swapToFitts();

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
    }

    void showToast(String text) {
        if (t != null) {
            t.cancel();
        }
        t = Toast.makeText(getContext(), text, Toast.LENGTH_LONG);
        t.show();

    }

    /**
     * Create a list with random
     *
     * @return list holding random points for the creation of the circles
     */
    private Point[] createConditions() {
        Point[] conditions = new Point[Constants.AMOUNT_ROWS * Constants.AMOUNT_COLLUMNS];

        // create all possible positions
        ArrayList<GridPosition> list = new ArrayList<GridPosition>();
        for (int i = 0; i < Constants.AMOUNT_ROWS; i++) {
            for (int j = 0; j < Constants.AMOUNT_COLLUMNS; j++) {
                list.add(new GridPosition(j, i));
            }
        }
        // shuffle them
        Collections.shuffle(list);

        int k = 0;
        for (GridPosition item : list) {
            float r = (float) item.getRow();
            float c = (float) item.getColumn();
            // calculate pixels per grid-position
            float row_s = (Constants.getScreenHeight() - crossSize) / Constants.AMOUNT_ROWS;
            float column_s = (Constants.getScreenWidth() - crossSize) / Constants.AMOUNT_COLLUMNS;
            // scale position with pixels
            int row = (int) (r * row_s);
            int column = (int) (c * column_s);
            conditions[k] = new Point(column, row);
            k++;
        }
        return conditions;
    }
    public interface ExchangeTouchFragment{
        void swapToFitts();
    }
}
