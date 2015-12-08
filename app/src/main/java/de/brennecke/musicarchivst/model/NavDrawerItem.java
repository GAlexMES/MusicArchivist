package de.brennecke.musicarchivst.model;

/**
 * Created by Alexander on 09.11.2015.
 */
public class NavDrawerItem {
    private String text;
    private int imageResource;

    public NavDrawerItem(String text, int resourceID){
        this.text=text;
        this.imageResource = resourceID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
