package fr.m2miage.webvideogen.controller;
import fr.m2miage.webvideogen.model.SimpleVideo;
import fr.m2miage.webvideogen.model.VideoAbs;
import fr.m2miage.webvideogen.services.VideogenService;
import fr.m2miage.webvideogen.util.CustomErrorType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VideoController
{

    private static final Logger log = Logger.getLogger(VideoController.class);

    @Autowired
    private VideogenService videogenService;

    //region Services de gestion de .videogen

    // ------------------- Envoi les noms des .videogens disponibles -------------------------------
    @CrossOrigin(origins = "http:/localhost:4200")
    @RequestMapping(value = "/videogens/", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getVideogens()
    {
        List<String> videogens = videogenService.getVideogens();
        if (videogens.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(videogens, HttpStatus.OK);
    }

    // ------------------- Change le .videogen utilisé par celui donnée en param --------------------
    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/videogens/", method = RequestMethod.POST)
    public ResponseEntity<?> setVideogen(@RequestParam(value = "filename") String target)
    {
        log.info("Remplacement du .videogen actuel par " + target);
        boolean result = videogenService.setVideoGen(target);
        if (!result)
        {
            String msg = "Le .videogen demandé n'existe pas";
            log.error(msg);
            return new ResponseEntity(new CustomErrorType(msg),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //endregion

    // ------------------- Envoi les videos contenues dans le .videogen -----------------------
    @CrossOrigin(origins = "http:/localhost:4200")
    @RequestMapping(value = "/videos/", method = RequestMethod.GET)
    public ResponseEntity<List<VideoAbs>> getAllVideos()
    {
        List<VideoAbs> list = videogenService.getVideos();
        if (list.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    //region Services de generation videos

    // ------------------- Genere une video en mode aléatoire et renvoi le lien -------------
    @CrossOrigin(origins = "http:/localhost:4200")
    @RequestMapping(value = "/generator/", method = RequestMethod.GET)
    public ResponseEntity<SimpleVideo> getGeneratedVideo()
    {
        SimpleVideo video = videogenService.generateVideo();
        log.info(video);
        if (video == null)
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(video, HttpStatus.OK);
    }


    // ------------------- Genere une video en mode configuré et renvoi le lien --------------
    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/generator/", method = RequestMethod.POST)
    public ResponseEntity<?> getConfiguratedVideo(@RequestBody List<String> target)
    {
        log.info("Generation de la video composée de : " + target);
        SimpleVideo video = videogenService.configureVideo(target);
        if (video == null)
        {
            String msg = "La video n'a pas pu être generee";
            log.error(msg);
            return new ResponseEntity(new CustomErrorType(msg),HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(video, HttpStatus.CREATED);
    }

    //endregion

    // ------------------- Genere un .gif et envoi le lien --------------------------------------
    @CrossOrigin(origins = "http:/localhost:4200")
    @RequestMapping(value = "/gif/", method = RequestMethod.GET)
    public ResponseEntity<String> createGif(@RequestBody SimpleVideo target)
    {
        String gif = videogenService.getGif(target);
        if (!gif.equals(""))
        {
            log.error("La generation du gif a échoué");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(gif, HttpStatus.OK);
    }

}