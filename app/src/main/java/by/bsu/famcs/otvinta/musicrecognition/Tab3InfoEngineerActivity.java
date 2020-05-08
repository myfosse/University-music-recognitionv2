package by.bsu.famcs.otvinta.musicrecognition;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class Tab3InfoEngineerActivity extends AppCompatActivity {

    private final Uri uriVK = Uri.parse("https://vk.com/betoyoung");
    private final Uri uriCall = Uri.parse("tel:+375 (44) 747-68-86");
    private final String mail = "vlad.kizenkov@mail.ru";

    private Button buttonEngineerVK;
    private Button buttonEngineerCall;
    private Button buttonEngineerMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab3_info_engineer_layout);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("AI & Server Engineer");

        buttonEngineerVK = findViewById(R.id.buttonEngineerVK);
        buttonEngineerCall = findViewById(R.id.buttonEngineerCall);
        buttonEngineerMail = findViewById(R.id.buttonEngineerMail);

        buttonEngineerVK.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, uriVK));
        });

        buttonEngineerCall.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_DIAL, uriCall));
        });

        buttonEngineerMail.setOnClickListener(view -> {
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