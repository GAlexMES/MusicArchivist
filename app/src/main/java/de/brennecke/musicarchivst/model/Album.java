package de.brennecke.musicarchivst.model;

/**
 * Created by Alexander on 25.10.2015.
 */
public class Album {
    private String artist;
    private String title;
    private String genre;

    public void setTitle(String title){
        this.title=title;
    }

    public String getTitle(){
        return title;
    }

    public void setArtist(String artist){
        this.artist = artist;
    }

    public String getArtist(){
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
