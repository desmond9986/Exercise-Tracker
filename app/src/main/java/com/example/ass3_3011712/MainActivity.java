package com.example.ass3_3011712;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private Chronometer chronometer;
    private LocationManager locMan;
    private GPXWriter gpxWriter;
    private long elapsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        chronometer = findViewById(R.id.chronometer);
        gpxWriter = new GPXWriter(new File(getExternalFilesDir(null)+"/GPStracks"));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn.getText().toString().equals("Start Tracker"))
                    startTracker();
                else
                    stopTracker();
            }
        });

    }

    private void startTracker(){
        btn.setText("Stop Tracker");
        gpxWriter.startWriting();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        locMan =(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        createLocationListener();
    }

    private void stopTracker(){
        btn.setText("Start Tracker");
        locMan = null;
        chronometer.stop();
        elapsed = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;
        chronometer.setBase(SystemClock.elapsedRealtime());
        gpxWriter.stopWriting();
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
        intent.putExtra("timer", elapsed);
        intent.putExtra("file name", gpxWriter.getFileName());
        startActivity(intent);
    }

    private void createLocationListener(){
        try{
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    gpxWriter.addLocation(location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getTime(), location.getSpeed());
                }

                @Override
                public void onProviderEnabled(String provider){

                    if(provider == LocationManager.GPS_PROVIDER){
                        //Show last known.
                        Location loc = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if(loc != null) {
                            //Now if the location is not null just show it.
                            gpxWriter.addLocation(loc.getLatitude(), loc.getLongitude(), loc.getAltitude(), loc.getTime(), loc.getSpeed());
                        }
                    }
                }

                @Override
                public void onProviderDisabled(String provider){

                    if(provider != LocationManager.GPS_PROVIDER){
                    }
                }
            });

        }// try
        catch(SecurityException se){
            se.printStackTrace();
        }//catch
    }


}