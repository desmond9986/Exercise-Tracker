package com.example.ass3_3011712;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn.getText().toString().equals("Start Tracker"))
                    btn.setText("Stop Tracker");
                else{
                    btn.setText("Start Tracker");
                    Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}