package org.chboston.cnlp.twitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tweet {

  private String tweetId = null;
  private Date tweetDate = null;
  private String message = null;
  private String user = null;
  private String profile = null;
  private String language = null;
  private String location = null;
  private String timezone = null;
  private double latitude;
  private double longitude;
  private String locationMarker = null;
  
  public Tweet(String rawText) throws ParseException{
    String[] parts = rawText.split("\t", -1);
    tweetId = parts[0];
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+00:00");
    tweetDate = sdf.parse(parts[1]);
    message = parts[2];
    user = parts[3];
    profile = parts[4];
    language = parts[5];
    location = parts[6];
    timezone = parts[7];
    longitude = Double.parseDouble(parts[8]);
    latitude = Double.parseDouble(parts[9]);
    locationMarker = parts[10];
  }
  
  public double[] getGeolocation(){
    return new double[]{ longitude, latitude };
  }
  
  public String getMessage(){
    return this.message;
  }
  
  public String getLocation(){
    return this.location;
  }
}
