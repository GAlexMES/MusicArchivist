package de.brennecke.musicarchivst.model;

/**
 * Created by Alexander on 02.01.2016.
 */
public class Track {

    String name;
    int trackNumber;
    int duration;

    public Track (String name){
        setName(name);
    }

    public Track(){
    }

    public void setName(String name){
        this.name=name;
    }

    public String getName(){
        return name;
    }

    public void setTrackNumber(int number){
        this.trackNumber=number;
    }

    public int getTrackNumber(){
        return trackNumber;
    }

    public int getDuration(){
        return duration;
    }

    public void setDuration(int duration){
        this.duration=duration;
    }
}

