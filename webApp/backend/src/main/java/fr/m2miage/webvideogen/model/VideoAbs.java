package fr.m2miage.webvideogen.model;

public abstract class VideoAbs
{

    private String id;
    private VideoType videoType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VideoType getVideoType() {
        return videoType;
    }

    public void setVideoType(VideoType videoType) {
        this.videoType = videoType;
    }

    public VideoAbs() {
    }

    public VideoAbs(String id) {
        this.id = id;
    }

    public VideoAbs(String id, VideoType videoType) {
        this.id = id;
        this.videoType = videoType;
    }

}
