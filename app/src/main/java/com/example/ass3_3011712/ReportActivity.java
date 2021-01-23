package com.example.ass3_3011712;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

public class ReportActivity extends AppCompatActivity {
    private TextView tv_speed, tv_distance, tv_time, tv_min_altitude, tv_max_altitude;
    private GPXReader gpxReader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        tv_speed = findViewById(R.id.tv_avg_spd);
        tv_distance = findViewById(R.id.tv_total_dst);
        tv_time = findViewById(R.id.tv_time_taken);
        tv_min_altitude = findViewById(R.id.tv_min_altitude);
        tv_max_altitude = findViewById(R.id.tv_max_altitude);

        try {
            gpxReader = new GPXReader(new FileReader(getExternalFilesDir(null) + "/GPStracks/" + getIntent().getStringExtra("file name")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        tv_speed.setText(gpxReader.getAverageSpeed() + "m/s");
        tv_time.setText(getIntent().getLongExtra("timer", 0) + "s");
        tv_min_altitude.setText(gpxReader.getMinAltitude() + "m");
        tv_max_altitude.setText(gpxReader.getMaxAltitude() + "m");

        DecimalFormat df = new DecimalFormat("#.##");
        tv_distance.setText(df.format(gpxReader.getTotalDistance()) + "m");
    }

    private String readFile(){
        // handle read file
        String readText ="";
        //String fileName = getFilesDir()+"textfile.txt";
        try {
            FileReader fr = new FileReader(getExternalFilesDir(null)+"/GPStracks/hi.gpx");
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) !=null){
                readText += line +"\n";
            }// while

            br.close();

        }//try
        catch(IOException ioe) {
            ioe.printStackTrace();
        }//catch
        return readText;

    }
}