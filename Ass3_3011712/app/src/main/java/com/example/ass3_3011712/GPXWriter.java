package com.example.ass3_3011712;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class GPXWriter {
    private File f;
    private BufferedWriter bw;
    private String fileName;

    public GPXWriter(File f){
        this.f = f;
        f.mkdirs(); // create the directory is not present
    }

    // a method to create a gpx file
    public void startWriting(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        fileName = sdf.format(Calendar.getInstance().getTime()); // use the current date time for the file name

        File file = new File(f, fileName + ".gpx");

        try {
            // write the format of gpx into the file
            FileWriter fw = new FileWriter(file,true);
            bw = new BufferedWriter(fw);
            bw.write("<gpx version=\"1.1\" creator=\"Ass3_3011712\">");
            bw.newLine();
            bw.write("\t<trk>");
            bw.newLine();
            bw.write("\t\t<name>Exercise Tracker</name>");
            bw.newLine();
            bw.write("\t\t<trkseg>");
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    // a method to write location into file
    public void addLocation(double latitude, double longitude, double altitude, long time, float speed){
        DecimalFormat df1 = new DecimalFormat("#.######");
        DecimalFormat df2 = new DecimalFormat("#.##");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        try{
            // write the details of location into the file with gpx format
            bw.newLine();
            bw.write("\t\t\t<trkpt lat=\"" + df1.format(latitude) + "\" lon=\"" + df1.format(longitude) + "\">");
            bw.newLine();
            bw.write("\t\t\t\t<ele>" + df2.format(altitude) + "</ele>");
            bw.newLine();
            bw.write("\t\t\t\t<time>" + sdf.format(new Date(time)) + "</time>");
            bw.newLine();
            bw.write("\t\t\t\t<speed>" + df2.format(speed) + "</speed>");
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    // a method to do write the last part of gpx and close the buffered writer
    public void stopWriting(){
        try {
            bw.newLine();
            bw.write("\t\t</trkseg>");
            bw.newLine();
            bw.write("\t</trk>");
            bw.newLine();
            bw.write("</gpx>");
            bw.flush();
            bw.close();
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    // a method to return the file name
    public String getFileName(){
        return fileName;
    }
}
