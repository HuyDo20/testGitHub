package com.example.fileaccesser;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ViewTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_text);
        TextView textView = findViewById(R.id.textView);
        String filePath = getIntent().getStringExtra("filePath");
        assert filePath != null;
        textView.setText(readTextFile(filePath));
        }

    public String readTextFile(String absoluteFilePath) {
        StringBuilder text = new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream(absoluteFilePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }
}