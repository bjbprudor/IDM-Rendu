package fr.m2miage.webvideogen.services;

import fr.m2miage.videogenFE.MyVideoGen;
import fr.m2miage.webvideogen.model.SimpleVideo;
import fr.m2miage.webvideogen.model.VideoAbs;
import fr.m2miage.webvideogen.model.VideoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.Media;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("videogenService")
public class VideogenService
{

    @Autowired
    private Environment env;

    private final String dataDir = "../data";
    private final String videogens = dataDir + "/videogens"; // dossier des .videogens
    private final String videos = dataDir + "/videos"; // dossier des videos
    private final String generatedVideos = dataDir + "/generated"; // dossier pour les videogenere
    private final String thumbnails = dataDir + "/thumbnails"; // dossier pour les thumbnails
    private final String gifs = dataDir + "/gifs"; // dossier pour les gifs

    private final String defaultVideogen = "../data/videogens/test.videogen"; // .videogen par default

    private File videoGen; // .videoGen selectionné

    public VideogenService()
    {
        setVideoGen("");
    }

    public List<String> getVideogens()
    {
        List<String> result = new ArrayList<>();
        try
        {
            File folder = new File(videogens); // on recupere le dossier des videogen
            result.addAll(Arrays.asList(folder.list())); // on recupere les .videogen
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    public boolean setVideoGen(String gen)
    {
        boolean res = false;
        try
        {
            File folder = new File(videogens); // recupere les videogens existant
            File[] files = folder.listFiles(); //
            if(files.length == 0) { throw new Exception("No .videogen has been found"); }
            for(File file : files)
            {
                // si gen est null on cherche le defaultvideogen
                // sinon on cherche gen
                res = (gen == "" && file.getName() == defaultVideogen)
                        || (gen != "" && file.getName() == gen);
                if(res) { this.videoGen = file; break; }
            }
            if(!res) { this.videoGen = files[0]; } // si le fichier n'a pas ete trouve on prend le premier .videogen
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return res;
    }

    public List<VideoAbs> getVideos()
    {
        List<VideoAbs> videos = new ArrayList<>();
        MyVideoGen mvg = new MyVideoGen();
        List<Media> medias = mvg.;
        // call jar
        // check thumbnail
        // if not exist create it
        return videos;
    }

    private boolean thumbnailExist(String target)
    {
        boolean result = false;
        File folder = new File(thumbnails);
        String[] files = folder.list();
        for (String file : files)
        {
            if(file.equals(target)) { result = true; break; }
        }
        return result;
    }

    public SimpleVideo generateVideo()
    {
        SimpleVideo generated = new SimpleVideo("generated", VideoType.Mandatory,"","");
        MyVideoGen mvg = new MyVideoGen();
        List<Media> medias = mvg.GetAllVideos(videoGen.getPath());
        // call jar, get video link
        return generated;
    }

    public SimpleVideo configureVideo(List<String> ids)
    {
        SimpleVideo configured = new SimpleVideo("configured", VideoType.Mandatory,"","");
        // call jar, configure
        return configured;
    }

    public String getGif(SimpleVideo video)
    {
        String path = "";
        // call jar, create gif
        return path;
    }

}
