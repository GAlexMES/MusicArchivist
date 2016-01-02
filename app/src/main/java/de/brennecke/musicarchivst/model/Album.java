package de.brennecke.musicarchivst.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;

/**
 * Created by Alexander on 25.10.2015.
 */
public class Album extends BaseObservable {
    private String artist ="";
    private String title="";
    private String genre="";
    private String albumCoverURL="";
    private Bitmap bitmap;
    private Tracklist tracklist;
    private long ID;

    public Album(Album album){
        this.artist=album.getArtist();
        this.title=album.getTitle();
        this.genre=album.getGenre();
        this.albumCoverURL=album.getAlbumCoverURL();
        this.bitmap=album.getCoverBitmap();
        this.tracklist=album.getTracklist();
        this.ID=album.getID();
    }
    public Album (){
        tracklist = new Tracklist();
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Bindable
    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAlbumCoverURL() {
        return albumCoverURL;
    }

    public void setAlbumCoverURL(String albumCoverURL) {
        this.albumCoverURL = albumCoverURL;
    }

    public Bitmap getCoverBitmap(){
        return bitmap;
    }

    public void setCoverBitmap(Bitmap bm){
        this.bitmap=bm;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setTracklist(Tracklist tracklist){
        this.tracklist=tracklist;
    }

    public Tracklist getTracklist(){
        return tracklist;
    }
}


