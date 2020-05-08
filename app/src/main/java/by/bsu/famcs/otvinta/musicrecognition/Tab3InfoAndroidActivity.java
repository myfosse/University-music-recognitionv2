package by.bsu.famcs.otvinta.musicrecognition;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class Tab3InfoAndroidActivity extends AppCompatActivity {

    private final Uri uriVK = Uri.parse("https://vk.com/myfosse");
    private final Uri uriCall = Uri.parse("tel:+375 (29) 396-06-75");
    private final String mail = "andrey.egorov.minsk@gmail.com";

    private Button buttonAndroidVK;
    private Button buttonAndroidCall;
    private Button buttonAndroidMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab3_info_android_layout);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Android Engineer Info");

        buttonAndroidVK = findViewById(R.id.buttonAndroidVK);
        buttonAndroidCall = findViewById(R.id.buttonAndroidCall);
        buttonAndroidMail = findViewById(R.id.buttonAndroidMail);


        buttonAndroidVK.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, uriVK));
        });

        buttonAndroidCall.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_DIAL, uriCall));
        });

        buttonAndroidMail.setOnClickListener(view -> {
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