package de.brennecke.musicarchivst.model;

/**
 * Created by Alexander on 06.12.2015.
 */
public class Exchange {

    private static Exchange INSTANCE = null;
    private Album currentAlbum;

    public static Exchange getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Exchange();
        }
        return INSTANCE;
    }

    public void setCurrentAlbum(Album a){
        currentAlbum = a;
    }

    public Album getCurrentAlbum(){
        return currentAlbum;
    }
}
