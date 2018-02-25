package fr.m2miage.webvideogen.model;

public class SimpleVideo extends VideoAbs
{

    private String link;
    private String thumbnail;

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

    public SimpleVideo(String id, VideoType videoType, String link, String thumbnail)
    {
        super(id);
        if(videoType == VideoType.Alternative)
        {
            System.out.println("Erreur impossible de mettre le type Alternative à SimpleVideo," +
                    " la video va devenir Optional");
            this.setVideoType(VideoType.Optional);
        }
        else { this.setVideoType(videoType); }
        this.link = link;
        this.thumbnail = thumbnail;
    }
}
