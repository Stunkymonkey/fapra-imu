package mci.fapra.fapra_imu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class TouchFragment extends Fragment {

    private ImageView cross;
    private TextView touchText = null;
    private Toast t;

    static TouchActivity touchActivity;

    ExchangeTouchFragment stf;

    private int crossSize = Constants.getTargetPixelsForPhone(10);
    private int iteration = 0;
    private int clicked_x = 0;
    private int clicked_y = 0;

    private Point[] conditions = createConditions();

    public static TouchFragment newInstance(TouchActivity activity){
        TouchFragment tf = new TouchFragment();
        touchActivity = activity;
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

        touchText = v.findViewById(R.id.touch_text);
        if (iteration>1){
            touchText.setVisibility(View.GONE);
        }

        cross = v.findViewById(R.id.cross);
        cross.setLayoutParams(new RelativeLayout.LayoutParams(crossSize, crossSize));

        final Point p = conditions[iteration];
        cross.setX(p.x);
        cross.setY(p.y);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchActivity.touchWriter.writeAction(System.currentTimeMillis(), clicked_x, clicked_y, p.x+(crossSize/2), p.y+(crossSize/2));
                //TODO maybe display rounds in an other way, e.g. in FittsFragment, so targets cannot be drawn underneath Toast
                showToast("Round: " + (iteration + 1) + "/" + Constants.AMOUNT_REPETITIONS);
                iteration++;
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
     * Create a list with points in a random order, covering a n x m grid defined in Constants.AMOUNT_ROWS/COLUMNS
     *
     * @return list holding points for the creation of the circles
     */
    public Point[] createConditions() {
        Point[] conditions = new Point[Constants.AMOUNT_ROWS * Constants.AMOUNT_COLUMNS];

        // create all possible positions
        ArrayList<GridPosition> list = new ArrayList<GridPosition>();
        for (int i = 0; i < Constants.AMOUNT_ROWS; i++) {
            for (int j = 0; j < Constants.AMOUNT_COLUMNS; j++) {
                list.add(new GridPosition(j, i));
            }
        }
        // shuffle them
        Collections.shuffle(list);

        int k = 0;
        for (GridPosition item : list) {
            int r = item.getRow();
            int c = item.getColumn();
            // calculate pixels per grid-position
            float row_s = (float) (Constants.getScreenHeight() - crossSize) / (Constants.AMOUNT_ROWS - 1);
            float column_s = (float) (Constants.getScreenWidth() - crossSize) / (Constants.AMOUNT_COLUMNS - 1);
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
