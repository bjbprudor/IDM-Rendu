package fr.m2miage.webvideogen.model;

import java.util.ArrayList;
import java.util.List;

public class AlternativeVideo extends VideoAbs
{

    private List<Alter> alters;

    public List<Alter> getAlters() {
        return alters;
    }

    public void setAlters(List<Alter> alters) {
        this.alters = alters;
    }

    public AlternativeVideo(String id) {
        super(id, VideoType.Alternative);
        this.alters = new ArrayList<>();
    }

    public AlternativeVideo(String id, List<Alter> alters) {
        super(id, VideoType.Alternative);
        this.alters = alters;
    }

}