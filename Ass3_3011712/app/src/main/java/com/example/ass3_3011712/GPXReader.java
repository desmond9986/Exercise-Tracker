package com.example.ass3_3011712;

import android.location.Location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GPXReader {
    private BufferedReader br;
    private ArrayList<Double> longitudes;
    private ArrayList<Double> latitudes;
    private ArrayList<String> times;
    private ArrayList<Float> speeds;
    private ArrayList<Float> altitudes;

    public GPXReader(FileReader fr){
        br = new BufferedReader(fr);

        // initial the variables
        longitudes = new ArrayList<Double>();
        latitudes = new ArrayList<Double>();
        times = new ArrayList<String>();
        speeds = new ArrayList<Float>();
        altitudes = new ArrayList<Float>();

        convertElements(); // save all the location details into variables
    }

    // save all the location details in the file to variables
    private void convertElements(){
        // create regular expression pattern to match the gpx format
        Pattern patternTrkpt = Pattern.compile("<trkpt lat=\"(\\d+\\.\\d{6})\" lon=\"(\\d+\\.\\d{6})\">");
        Pattern patternEle = Pattern.compile("<ele>(\\d+\\.\\d{2})</ele>");
        Pattern patternTime = Pattern.compile("<time>(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z)</time>");
        Pattern patternSpeed = Pattern.compile("<speed>(\\d+.\\d{2})</speed>");

        try {
            String line;
            while ((line = br.readLine()) !=null){
                Matcher m;
                // use the patterns declared above to find the location details
                if((m = patternTrkpt.matcher(line)).find())
                {
                    latitudes.add(Double.parseDouble(m.group(1)));
                    longitudes.add(Double.parseDouble(m.group(2)));
                }
                else if((m = patternEle.matcher(line)).find())
                    altitudes.add(Float.parseFloat(m.group(1)));
                else if((m = patternTime.matcher(line)).find())
                    times.add(m.group(1));
                else if((m = patternSpeed.matcher(line)).find())
                    speeds.add(Float.parseFloat(m.group(1)));

            }// while

            br.close();

        }//try
        catch(IOException ioe) {
            ioe.printStackTrace();
        }//catch
    }

    // compute the average speed and return it
    public double getAverageSpeed(){
        double avgSpd = 0.0;

        for(int i = 0; i < speeds.size(); i++)
            avgSpd += speeds.get(i);

        avgSpd /= (double)speeds.size();

        return avgSpd;
    }

    // compute total distance and return it
    public float getTotalDistance(){
        float totalDistance = 0;
        float[] distance = new float[1];

        for(int i = 0; i < longitudes.size() - 1; i++){
            // use th Location.distanceBetween() to find the distance
            Location.distanceBetween(latitudes.get(i), longitudes.get(i), latitudes.get(i+1), longitudes.get(i+1), distance);
            totalDistance += distance[0];
        }

        return totalDistance;
    }

    // compute a maximum altitude and return in
    public float getMaxAltitude(){
        float maxAltitude = 0;

        for(int i = 0; i < altitudes.size(); i++){
            if(i == 0)
                maxAltitude = altitudes.get(i);
            if(maxAltitude < altitudes.get(i))
                maxAltitude = altitudes.get(i);
        }

        return maxAltitude;
    }


    // compute the minimum altitude and return it
    public float getMinAltitude(){
        float minAltitude = 0;

        for(int i = 0; i < altitudes.size(); i++){
            if(i == 0)
                minAltitude = altitudes.get(i);
            if(minAltitude > altitudes.get(i))
                minAltitude = altitudes.get(i);
        }

        return minAltitude;
    }

    public ArrayList<Double> getLongitudes() {
        return longitudes;
    }

    public ArrayList<Double> getLatitudes() {
        return latitudes;
    }

    public ArrayList<String> getTimes() {
        return times;
    }

    public ArrayList<Float> getSpeeds() {
        return speeds;
    }

    public ArrayList<Float> getAltitudes() {
        return altitudes;
    }
}
