package by.bsu.famcs.otvinta.musicrecognition;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

public class Tab3InfoActivity extends Activity {

    private Button buttonLocation;
    private Button buttonMainEngineer;
    private Button buttonEngineer;
    private Button buttonAndroid;
    private Button buttonMidi;

    private final Uri uriLocation = Uri.parse("geo:53.893418, 27.545374");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab3_info_layout);

        buttonLocation = findViewById(R.id.buttonLocation);
        buttonMainEngineer = findViewById(R.id.buttonMainEngineer);
        buttonEngineer = findViewById(R.id.buttonEngineer);
        buttonAndroid = findViewById(R.id.buttonAndroid);
        buttonMidi = findViewById(R.id.buttonMidi);

        buttonLocation.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, uriLocation));
        });

        buttonMainEngineer.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Tab3InfoMainEngineerActivity.class));
        });

        buttonEngineer.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Tab3InfoEngineerActivity.class));
        });

        buttonAndroid.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Tab3InfoAndroidActivity.class));
        });

        buttonMidi.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Tab3InfoMidiActivity.class));
        });
    }
}
