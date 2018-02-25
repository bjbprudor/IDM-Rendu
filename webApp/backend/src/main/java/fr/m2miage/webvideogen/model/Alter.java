package fr.m2miage.webvideogen.model;

public class Alter
{
    private String id;
    private String link;
    private String thumbnail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Alter() {
    }

    public Alter(String id, String link, String thumbnail) {
        this.id = id;
        this.link = link;
        this.thumbnail = thumbnail;
    }

}
