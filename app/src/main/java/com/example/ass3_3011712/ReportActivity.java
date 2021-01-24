package com.example.ass3_3011712;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class ReportActivity extends AppCompatActivity {
    private TextView tv_speed, tv_distance, tv_time, tv_min_altitude, tv_max_altitude;
    private GPXReader gpxReader;
    private SpeedGraph speedGraph;

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
            gpxReader = new GPXReader(new FileReader(getExternalFilesDir(null) + "/GPStracks/" + getIntent().getStringExtra("file name") + ".gpx"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        tv_speed.setText(df.format(gpxReader.getAverageSpeed()) + "m/s");
        tv_time.setText(getIntent().getLongExtra("timer", 0) + "s");
        tv_min_altitude.setText(gpxReader.getMinAltitude() + "m");
        tv_max_altitude.setText(gpxReader.getMaxAltitude() + "m");
        tv_distance.setText(df.format(gpxReader.getTotalDistance()) + "m");

        speedGraph = findViewById(R.id.speed_graph);
        generateGraph();
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

    private void generateGraph(){
        ArrayList<Float> kmPerHour = new ArrayList<Float>();
        ArrayList<Integer> seconds = new ArrayList<Integer>();
        ArrayList<Float> mPerSecond = gpxReader.getSpeeds();
        ArrayList<String> times = gpxReader.getTimes();

        speedGraph = findViewById(R.id.speed_graph);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date startTime = sdf.parse(getIntent().getStringExtra("file name"));

            for(int i = 0; i < mPerSecond.size(); i++){
                // convert m/s to km/h
                kmPerHour.add((float)(mPerSecond.get(i) * 3.6));
                seconds.add((int)(startTime.getTime() - sdf.parse(times.get(i)).getTime()) / 1000);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        speedGraph.setData(kmPerHour, seconds);
        speedGraph.postInvalidate();
    }
}