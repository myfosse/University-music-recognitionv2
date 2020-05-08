package by.bsu.famcs.otvinta.musicrecognition;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class Tab3InfoMainEngineerActivity extends AppCompatActivity {

    private final Uri uriVK = Uri.parse("https://vk.com/slawlad");
    private final Uri uriCall = Uri.parse("tel:+375 (29) 253-55-15");
    private final String mail = "vladementei@gmail.com";

    private Button buttonMainEngineerVK;
    private Button buttonMainEngineerCall;
    private Button buttonMainEngineerMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab3_info_main_engineer_layout);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Main AI & Server Engineer");

        buttonMainEngineerVK = findViewById(R.id.buttonMainEngineerVK);
        buttonMainEngineerCall = findViewById(R.id.buttonMainEngineerCall);
        buttonMainEngineerMail = findViewById(R.id.buttonMainEngineerMail);

        buttonMainEngineerVK.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, uriVK));
        });

        buttonMainEngineerCall.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_DIAL, uriCall));
        });

        buttonMainEngineerMail.setOnClickListener(view -> {
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
