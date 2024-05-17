package com.example.fileaccesser;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MusicPlayActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private TextView runningTime_tv;
    private TextView durationTime_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        mediaPlayer = new MediaPlayer();
        try {
            String filePath = getIntent().getStringExtra("filePath");
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        seekBar = findViewById(R.id.musicSeekbar);
        runningTime_tv = findViewById(R.id.runningTime_tv);
        durationTime_tv = findViewById(R.id.durationTime_tv);

        int duration = mediaPlayer.getDuration();
        int minutes = (duration / 1000) / 60;
        int seconds = (duration / 1000) % 60;
        String DurationTime = String.format("%d:%02d", minutes, seconds);

        durationTime_tv.setText("" + DurationTime);

        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Handle when the user starts touching the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Button playButton = findViewById(R.id.playButton);
                mediaPlayer.start();
                updateRunningTime();
                playButton.setText("Play");
            }
        });

        mediaPlayer.start();
        updateRunningTime();

        Button playButton = findViewById(R.id.playButton);
        Button pauseButton = findViewById(R.id.pauseButton);
        Button stopButton = findViewById(R.id.stopButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                if (playButton.getText()=="Resume" || !mediaPlayer.isPlaying()) {
                    playButton.setText("Play");

                }
                updateRunningTime();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playButton.setText("Resume");
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    try {
                        mediaPlayer.prepare();
                        seekBar.setProgress(0);
                        updateRunningTime();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    Handler handler = new Handler();
    private void updateRunningTime() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                    handler.postDelayed(this, 1000);
                    int minutes = (currentPosition / 1000) / 60;
                    int seconds = (currentPosition / 1000) % 60;
                    String runningTime = String.format("%d:%02d", minutes, seconds);
                    runningTime_tv.setText(runningTime);
                }
            }
        }, 0);
    }
}