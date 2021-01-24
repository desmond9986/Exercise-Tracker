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
    private Button btn; // start button
    private Chronometer chronometer; // timer
    private LocationManager locMan; // location manager
    private LocationListener locationListener; // location listener
    private GPXWriter gpxWriter; // GPXWriter used to write location track points to gpx file
    private TextView tv_speed; // speed meter
    private long elapsed; // time taken in exercise
    private Location lastLocation; // last location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initial variables
        locMan =(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        btn = findViewById(R.id.btn);
        chronometer = findViewById(R.id.chronometer);
        tv_speed = findViewById(R.id.tv_spd);
        gpxWriter = new GPXWriter(new File(getExternalFilesDir(null)+"/GPStracks")); // initial the gpxWriter with a path argument

        // create a listener for button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if the current state of button is start tracker
                if(btn.getText().toString().equals("Start Tracker"))
                    startTracker();
                else
                    stopTracker();
            }
        });
    }

    // a method will be called every time the start button is pressed
    private void startTracker(){
        // check if the GPS is enabled in device
        if(!locationEnabledChecker())
            return;

        lastLocation = null; // initial and reset the last location
        btn.setText("Stop Tracker");

        gpxWriter.startWriting(); // start to generate a gpx file

        chronometer.setBase(SystemClock.elapsedRealtime()); // set the timer's base to current total time after device booted
        chronometer.start(); // timer start to count

        createLocationListener(); // create a location listener
    }

    // a method will be called every time the stop button is pressed
    private void stopTracker(){
        btn.setText("Start Tracker"); // change the text of button to stop tracker
        locMan.removeUpdates(locationListener); // remove the location listener

        chronometer.stop(); // stop the timer
        elapsed = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000; // get the total time of the journey
        chronometer.setBase(SystemClock.elapsedRealtime());

        gpxWriter.stopWriting(); // close time gpx file

        tv_speed.setText("0m/s");

        // create intent and put extras and start activity
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
        intent.putExtra("timer", elapsed);
        intent.putExtra("file name", gpxWriter.getFileName());
        startActivity(intent);
    }

    // a method to create location listener
    private void createLocationListener(){
        try{
            // set the min time to 0.5s and the min distance to 0m
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0f,
                    locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    float speed;
                    // if the last location is null then the current speed is 0, otherwise count the speed of this lacation
                    if(lastLocation == null)
                        speed = 0;
                    else
                        speed = calculateSpeedInMeter(location);

                    // tell the gpxWriter to write the location in file
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

    // a method to check if the location is enabled and generate alert dialog if is not enable
    private boolean locationEnabledChecker(){
        boolean gps_enabled = false;

        try {
            // check if the location is enabled
            gps_enabled = locMan.isProviderEnabled(LocationManager. GPS_PROVIDER ) ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }

        if (!gps_enabled ) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is off");
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
        return gps_enabled; // return the boolean
    }

    // calculate the speed of the location
    private float calculateSpeedInMeter(Location currentLoc){
        float speed = (float) Math.abs(lastLocation.distanceTo(currentLoc)/((lastLocation.getTime()-currentLoc.getTime()) / 1000));
        return speed;
    }
}