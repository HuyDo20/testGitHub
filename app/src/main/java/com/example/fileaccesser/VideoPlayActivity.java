package com.example.fileaccesser;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoPlayActivity extends AppCompatActivity {
    private VideoView videoView;
    private TextView timeTextView;
    private Button playButton, pauseButton, skip;
    private SeekBar seekBar;

    private void updateTimeTextView(int progress) {
        int minutes = (progress / 1000) / 60;
        int seconds = (progress / 1000) % 60;
        String time = String.format("%d:%02d", minutes, seconds);
        timeTextView.setText(time);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        videoView = findViewById(R.id.videoView);
        String filePath = getIntent().getStringExtra("filePath");

        videoView.setVideoPath(filePath);


        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        skip = findViewById(R.id.skipBtn);
        seekBar = findViewById(R.id.seekBar);
        timeTextView = findViewById(R.id.timeTextView);


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = videoView.getCurrentPosition();
                int newPosition = currentPosition + 30000;
                videoView.seekTo(newPosition);
            }
        });


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                }
            }
        });


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int duration = videoView.getDuration();
                seekBar.setMax(duration);
                seekBar.setVisibility(View.VISIBLE);

                videoView.start();
            }
        });


        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int currentPosition = videoView.getCurrentPosition();
                            handler.postDelayed(this, 1000);
                            seekBar.setProgress(videoView.getCurrentPosition());
                            updateTimeTextView(currentPosition);
                        }
                    }, 1000);
                }
                return false;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(videoView.isPlaying()){
                    videoView.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int selectedTime = seekBar.getProgress();
                videoView.seekTo(selectedTime);
                videoView.start();
            }
        });
    }
}