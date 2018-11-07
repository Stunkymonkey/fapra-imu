package mci.fapra.fapra_imu;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private final String TAG = Writer.class.getName();

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WAKE_LOCK};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, permissions, 1);


        final EditText participantId = (EditText) findViewById(R.id.pId);
        final Button start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s_pID = participantId.getText().toString();
                Log.d(TAG, s_pID);
                if (s_pID.length() != 0) {
                    int pID = Integer.parseInt(s_pID);
                    if (pID > 0) {
                        launchActivity();
                    }
                }
            }
        });

    }

    private void launchActivity() {
        Intent intent = new Intent(this, TouchActivity.class);
        startActivity(intent);
    }
}
