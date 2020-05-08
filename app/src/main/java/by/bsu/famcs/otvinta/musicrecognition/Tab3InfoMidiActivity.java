package by.bsu.famcs.otvinta.musicrecognition;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class Tab3InfoMidiActivity extends AppCompatActivity {

    private final Uri uriVK = Uri.parse("https://vk.com/tans2000");
    private final Uri uriCall = Uri.parse("tel:+375 (44) 727-01-10");
    private final String mail = "tpolet2000@gmail.com";

    private Button buttonMidiVK;
    private Button buttonMidiCall;
    private Button buttonMidiMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab3_info_midi_layout);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Midi Engineer");

        buttonMidiVK = findViewById(R.id.buttonMidiVK);
        buttonMidiCall = findViewById(R.id.buttonMidiCall);
        buttonMidiMail = findViewById(R.id.buttonMidiMail);

        buttonMidiVK.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, uriVK));
        });

        buttonMidiCall.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_DIAL, uriCall));
        });

        buttonMidiMail.setOnClickListener(view -> {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
            email.putExtra(Intent.EXTRA_SUBJECT, "Theme");
            email.putExtra(Intent.EXTRA_TEXT, "");
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Choose mail application:"));
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
