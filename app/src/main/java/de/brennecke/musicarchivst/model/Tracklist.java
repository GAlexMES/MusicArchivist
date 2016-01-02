package de.brennecke.musicarchivst.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 02.01.2016.
 */
public class Tracklist {
    public List<Track> tracks;

    public Tracklist(List<Track> tracks){
        this.tracks = tracks;
    }

    public Tracklist(){
        tracks = new ArrayList<>();
    }

    public void addTrack(Track t){
        tracks.add(t);
    }

    public List<Track> getTracks(){
        return tracks;
    }

    public String getTitles(){
        String retval = "";
        for(Track t : tracks){
            retval = retval + t.getName() +"\n";
        }
        return retval;
    }
}
