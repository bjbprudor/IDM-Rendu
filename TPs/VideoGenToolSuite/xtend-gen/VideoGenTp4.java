import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.junit.Assert;
import org.junit.Test;
import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq;
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq;
import org.xtext.example.mydsl.videoGen.Media;
import org.xtext.example.mydsl.videoGen.OptionalVideoSeq;
import org.xtext.example.mydsl.videoGen.VideoDescription;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;

/**
 * TP3
 */
@SuppressWarnings("all")
public class VideoGenTp4 {
  public static void main(final String[] args) {
    VideoGenHelper _videoGenHelper = new VideoGenHelper();
    VideoGenTp4.addText(_videoGenHelper, "ANIME POWER");
  }
  
  /**
   * Q1
   */
  @Test
  public void testNbVariantes() {
    final VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
    Assert.assertEquals(this.nbVariantes(videoGen), this.listVariantVideos(videoGen).size());
    int _nbVariantes = this.nbVariantes(videoGen);
    String _plus = ("Test Q1 : " + Integer.valueOf(_nbVariantes));
    String _plus_1 = (_plus + " == ");
    int _size = this.listVariantVideos(videoGen).size();
    String _plus_2 = (_plus_1 + Integer.valueOf(_size));
    InputOutput.<String>println(_plus_2);
  }
  
  /**
   * Q2
   */
  @Test
  public void testNbVariantesForManyVideoGenFles() {
    VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example2.videogen"));
    Assert.assertEquals(this.nbVariantes(videoGen), this.listVariantVideos(videoGen).size());
    int _nbVariantes = this.nbVariantes(videoGen);
    String _plus = ("Test Q2 : " + Integer.valueOf(_nbVariantes));
    String _plus_1 = (_plus + " == ");
    int _size = this.listVariantVideos(videoGen).size();
    String _plus_2 = (_plus_1 + Integer.valueOf(_size));
    InputOutput.<String>println(_plus_2);
    videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example3.videogen"));
    Assert.assertEquals(this.nbVariantes(videoGen), this.listVariantVideos(videoGen).size());
    int _nbVariantes_1 = this.nbVariantes(videoGen);
    String _plus_3 = ("Test Q2 : " + Integer.valueOf(_nbVariantes_1));
    String _plus_4 = (_plus_3 + " == ");
    int _size_1 = this.listVariantVideos(videoGen).size();
    String _plus_5 = (_plus_4 + Integer.valueOf(_size_1));
    InputOutput.<String>println(_plus_5);
    videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example4.videogen"));
    Assert.assertEquals(this.nbVariantes(videoGen), this.listVariantVideos(videoGen).size());
    int _nbVariantes_2 = this.nbVariantes(videoGen);
    String _plus_6 = ("Test Q2 : " + Integer.valueOf(_nbVariantes_2));
    String _plus_7 = (_plus_6 + " == ");
    int _size_2 = this.listVariantVideos(videoGen).size();
    String _plus_8 = (_plus_7 + Integer.valueOf(_size_2));
    InputOutput.<String>println(_plus_8);
    videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example5.videogen"));
    Assert.assertEquals(this.nbVariantes(videoGen), this.listVariantVideos(videoGen).size());
    int _nbVariantes_3 = this.nbVariantes(videoGen);
    String _plus_9 = ("Test Q2 : " + Integer.valueOf(_nbVariantes_3));
    String _plus_10 = (_plus_9 + " == ");
    int _size_3 = this.listVariantVideos(videoGen).size();
    String _plus_11 = (_plus_10 + Integer.valueOf(_size_3));
    InputOutput.<String>println(_plus_11);
  }
  
  @Test
  public void testNbLinesInCsv() {
    try {
      final VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
      FileReader _fileReader = new FileReader("videos-size.csv");
      BufferedReader br = new BufferedReader(_fileReader);
      String line = br.readLine();
      int nbLines = 0;
      while (((line = br.readLine()) != null)) {
        nbLines++;
      }
      Assert.assertEquals(this.nbVariantes(videoGen), nbLines);
      int _nbVariantes = this.nbVariantes(videoGen);
      String _plus = ("Test Q3 : " + Integer.valueOf(_nbVariantes));
      String _plus_1 = (_plus + " == ");
      String _plus_2 = (_plus_1 + Integer.valueOf(nbLines));
      InputOutput.<String>println(_plus_2);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Q4
   */
  @Test
  public void testNbFramesGenerated() {
    final VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
    int nbVideos = 0;
    final EList<Media> videos = videoGen.getMedias();
    for (final Media video : videos) {
      if ((video instanceof MandatoryVideoSeq)) {
        nbVideos++;
      } else {
        if ((video instanceof OptionalVideoSeq)) {
          nbVideos++;
        } else {
          if ((video instanceof AlternativeVideoSeq)) {
            EList<VideoDescription> _videodescs = ((AlternativeVideoSeq)video).getVideodescs();
            for (final VideoDescription videodesc : _videodescs) {
              nbVideos++;
            }
          }
        }
      }
    }
    final File folder = new File("frames/");
    Assert.assertEquals(nbVideos, ((List<File>)Conversions.doWrapArray(folder.listFiles())).size());
    int _size = ((List<File>)Conversions.doWrapArray(folder.listFiles())).size();
    String _plus = ((("Test Q4 : " + Integer.valueOf(nbVideos)) + " == ") + Integer.valueOf(_size));
    InputOutput.<String>println(_plus);
  }
  
  /**
   * Q4bis
   */
  @Test
  public void testNbFramesInHtml() {
    try {
      final VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
      int nbVideos = 0;
      final EList<Media> videos = videoGen.getMedias();
      for (final Media video : videos) {
        if ((video instanceof MandatoryVideoSeq)) {
          nbVideos++;
        } else {
          if ((video instanceof OptionalVideoSeq)) {
            nbVideos++;
          } else {
            if ((video instanceof AlternativeVideoSeq)) {
              EList<VideoDescription> _videodescs = ((AlternativeVideoSeq)video).getVideodescs();
              for (final VideoDescription videodesc : _videodescs) {
                nbVideos++;
              }
            }
          }
        }
      }
      FileReader _fileReader = new FileReader("generateFrames.html");
      BufferedReader br = new BufferedReader(_fileReader);
      String line = "";
      int nbFramesInHtml = 0;
      while (((line = br.readLine()) != null)) {
        boolean _contains = line.contains("<img src");
        if (_contains) {
          nbFramesInHtml++;
        }
      }
      InputOutput.<String>println(((("Test Q4bis : " + Integer.valueOf(nbVideos)) + " == ") + Integer.valueOf(nbFramesInHtml)));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * For Q1-Q2-Q3 true test to determine the number of video variant
   */
  public int nbVariantes(final VideoGeneratorModel videoGen) {
    int nbVariante = 1;
    final EList<Media> videos = videoGen.getMedias();
    for (final Media video : videos) {
      if ((video instanceof OptionalVideoSeq)) {
        nbVariante = (nbVariante * 2);
      } else {
        if ((video instanceof AlternativeVideoSeq)) {
          int _size = ((AlternativeVideoSeq)video).getVideodescs().size();
          int _multiply = (nbVariante * _size);
          nbVariante = _multiply;
        }
      }
    }
    return nbVariante;
  }
  
  /**
   * For Q1-Q2 : list of videos variant
   */
  public List<ArrayList<VideoDescription>> listVariantVideos(final VideoGeneratorModel videoGen) {
    ArrayList<ArrayList<VideoDescription>> listVariantVideos = new ArrayList<ArrayList<VideoDescription>>();
    ArrayList<VideoDescription> _arrayList = new ArrayList<VideoDescription>();
    listVariantVideos.add(_arrayList);
    EList<Media> _medias = videoGen.getMedias();
    for (final Media video : _medias) {
      if ((video instanceof MandatoryVideoSeq)) {
        for (final ArrayList<VideoDescription> list : listVariantVideos) {
          list.add(((MandatoryVideoSeq)video).getDescription());
        }
      } else {
        if ((video instanceof OptionalVideoSeq)) {
          ArrayList<ArrayList<VideoDescription>> listTmp = new ArrayList<ArrayList<VideoDescription>>();
          for (final ArrayList<VideoDescription> list_1 : listVariantVideos) {
            {
              Object _clone = list_1.clone();
              ArrayList<VideoDescription> tmp = ((ArrayList<VideoDescription>) _clone);
              listTmp.add(list_1);
              tmp.add(((OptionalVideoSeq)video).getDescription());
              listTmp.add(tmp);
            }
          }
          listVariantVideos = listTmp;
        } else {
          if ((video instanceof AlternativeVideoSeq)) {
            ArrayList<ArrayList<VideoDescription>> listTmp_1 = new ArrayList<ArrayList<VideoDescription>>();
            EList<VideoDescription> _videodescs = ((AlternativeVideoSeq)video).getVideodescs();
            for (final VideoDescription vids : _videodescs) {
              for (final ArrayList<VideoDescription> list_2 : listVariantVideos) {
                {
                  Object _clone = list_2.clone();
                  ArrayList<VideoDescription> tmp = ((ArrayList<VideoDescription>) _clone);
                  tmp.add(vids);
                  listTmp_1.add(tmp);
                }
              }
            }
            listVariantVideos = listTmp_1;
          }
        }
      }
    }
    return listVariantVideos;
  }
  
  /**
   * Q7
   */
  public static void addText(final VideoGenHelper videoGen, final String text) {
    try {
      final FileWriter file = new FileWriter("playlistFfmpeg.txt");
      final VideoGeneratorModel videogen = videoGen.loadVideoGenerator(URI.createURI("example1.videogen"));
      final Random random = new Random();
      file.write("# playlist to concat videos with ffmpeg\n");
      final Consumer<Media> _function = (Media video) -> {
        try {
          if ((video instanceof MandatoryVideoSeq)) {
            String _location = ((MandatoryVideoSeq)video).getDescription().getLocation();
            String _plus = ("file \'" + _location);
            String _plus_1 = (_plus + "\'\n");
            file.write(_plus_1);
          } else {
            if ((video instanceof OptionalVideoSeq)) {
              boolean _nextBoolean = random.nextBoolean();
              if (_nextBoolean) {
                String _location_1 = ((OptionalVideoSeq)video).getDescription().getLocation();
                String _plus_2 = ("file \'" + _location_1);
                String _plus_3 = (_plus_2 + "\'\n");
                file.write(_plus_3);
              }
            } else {
              if ((video instanceof AlternativeVideoSeq)) {
                String _location_2 = ((AlternativeVideoSeq)video).getVideodescs().get(random.nextInt(((AlternativeVideoSeq)video).getVideodescs().size())).getLocation();
                String _plus_4 = ("file \'" + _location_2);
                String _plus_5 = (_plus_4 + "\'\n");
                file.write(_plus_5);
              }
            }
          }
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      };
      videogen.getMedias().forEach(_function);
      file.close();
      Process p = Runtime.getRuntime().exec(
        (((((("cmd /c start cmd.exe " + "/K \"ffmpeg -f concat -safe 0 -i playlistFfmpeg.txt ") + "-vf drawtext=\"fontsize=30:fontfile=C\\:\\Windows\\Fonts\\FreeSerif.ttf:text=\'") + text) + "\':x=(w-text_w)/2:y=(h-text_h)/2\" ") + "-crf 16 output_concat_with_text.mp4") + "&& exit\""));
      InputOutput.<String>println(
        (((("ffmpeg -f concat -safe 0 -i playlistFfmpeg.txt " + "-vf drawtext=\"fontsize=30:fontfile=C\\:\\Windows\\Fonts\\FreeSerif.ttf:text=\'") + text) + "\':x=(w-text_w)/2:y=(h-text_h)/2\" ") + "-crf 16 output_concat_with_text.mp4"));
      p.waitFor();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Q5 :
   * Q6 :
   */
  public void verifierErreurs() {
  }
}
