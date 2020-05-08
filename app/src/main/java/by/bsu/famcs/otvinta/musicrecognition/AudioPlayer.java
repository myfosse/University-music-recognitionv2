package by.bsu.famcs.otvinta.musicrecognition;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class AudioPlayer extends AppCompatActivity {

    static MediaPlayer mediaPlayer;

    private int position;
    private SeekBar seekBar;
    private ArrayList<File> songList;
    private Thread updateSeekBar;

    private Button previousButton;
    private Button pauseButton;
    private Button nextButton;
    private TextView songNameText;

    private String songName;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audioplayer_layout);

        songNameText = findViewById(R.id.txtSongLabel);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Now Playing");

        previousButton = findViewById(R.id.previous);
        pauseButton = findViewById(R.id.pause);
        nextButton = findViewById(R.id.next);

        seekBar = findViewById(R.id.seekBar);

        updateSeekBar = new Thread() {
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;
                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    } catch (InterruptedException ignored) {

                    }
                }
            }
        };

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i = getIntent();
        Bundle b = i.getExtras();

        songList = (ArrayList) b.getParcelableArrayList("songs");

        songName = songList.get(position).getName();

        String SongName = i.getStringExtra("songname");
        songNameText.setText(SongName);
        songNameText.setSelected(true);

        position = b.getInt("pos", 0);
        Uri u = Uri.parse(songList.get(position).toString());

        mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        updateSeekBar.start();
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        pauseButton.setOnClickListener(v -> {
            seekBar.setMax(mediaPlayer.getDuration());
            if (mediaPlayer.isPlaying()) {
                pauseButton.setBackgroundResource(R.drawable.ic_tab2_audio_play);
                mediaPlayer.pause();

            } else {
                pauseButton.setBackgroundResource(R.drawable.ic_tab2_audio_pause);
                mediaPlayer.start();
            }
        });

        nextButton.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % songList.size());
            Uri u1 = Uri.parse(songList.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), u1);

            songName = songList.get(position).getName();
            songNameText.setText(songName);

            try {
                seekBar.setProgress(0);
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
            } catch (Exception ignored) {
            }

        });

        previousButton.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();

            position = ((position - 1) < 0) ? (songList.size() - 1) : (position - 1);
            Uri u12 = Uri.parse(songList.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), u12);
            songName = songList.get(position).getName();
            songNameText.setText(songName);
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.start();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}