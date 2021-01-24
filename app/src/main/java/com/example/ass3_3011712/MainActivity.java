package com.example.ass3_3011712;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private Chronometer chronometer;
    private LocationManager locMan;
    private LocationListener locationListener;
    private GPXWriter gpxWriter;
    private TextView tv_speed;
    private long elapsed;
    private Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locMan =(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        btn = findViewById(R.id.btn);
        chronometer = findViewById(R.id.chronometer);
        tv_speed = findViewById(R.id.tv_spd);
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
        if(!locationEnabledChecker())
            return;
        lastLocation = null;
        btn.setText("Stop Tracker");
        gpxWriter.startWriting();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        createLocationListener();
    }

    private void stopTracker(){
        btn.setText("Start Tracker");
        locMan.removeUpdates(locationListener);
        chronometer.stop();
        elapsed = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;
        chronometer.setBase(SystemClock.elapsedRealtime());
        gpxWriter.stopWriting();
        tv_speed.setText("0m/s");
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
        intent.putExtra("timer", elapsed);
        intent.putExtra("file name", gpxWriter.getFileName());
        startActivity(intent);
    }

    private void createLocationListener(){
        try{
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0f,
                    locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    float speed;
                    if(lastLocation == null)
                        speed = 0;
                    else
                        speed = calculateSpeedInMeter(location);
                    gpxWriter.addLocation(location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getTime(), speed);
                    DecimalFormat df = new DecimalFormat("#.###");
                    tv_speed.setText(df.format(speed)+ "m/s");
                    lastLocation = location;
                }

                @Override
                public void onProviderEnabled(String provider){

                    if(provider == LocationManager.GPS_PROVIDER){
                        //Show last known.
                        Location loc = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if(loc != null) {
                            //Now if the location is not null just show it.
                            float speed;
                            if(lastLocation == null)
                                speed = 0;
                            else
                                speed = calculateSpeedInMeter(loc);
                            gpxWriter.addLocation(loc.getLatitude(), loc.getLongitude(), loc.getAltitude(), loc.getTime(), loc.getSpeed());
                            DecimalFormat df = new DecimalFormat("#.###");
                            tv_speed.setText(df.format(speed)+ "m/s");
                            lastLocation = loc;
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

    // check if the location is enabled
    private boolean locationEnabledChecker(){
        boolean gps_enabled = false;

        try {
            gps_enabled = locMan.isProviderEnabled(LocationManager. GPS_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }

        if (!gps_enabled ) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is ");
            // Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
            // On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            // Showing Alert Message
            alertDialog.show();
        }
        return gps_enabled;
    }

    private float calculateSpeedInMeter(Location currentLoc){
        float speed = (float) Math.abs(lastLocation.distanceTo(currentLoc)/((lastLocation.getTime()-currentLoc.getTime()) / 1000));
        return speed;
    }
}