import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq;
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq;
import org.xtext.example.mydsl.videoGen.Media;
import org.xtext.example.mydsl.videoGen.OptionalVideoSeq;
import org.xtext.example.mydsl.videoGen.VideoDescription;
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel;

/**
 * TP2
 */
@SuppressWarnings("all")
public class VideoGenTp2 {
  private static String player = "ffplay.exe -autoexit -an ";
  
  public static void main(final String[] args) {
    VideoGenHelper _videoGenHelper = new VideoGenHelper();
    VideoGenTp2.modelToTextWithFfmpeg(_videoGenHelper);
  }
  
  /**
   * Q1
   */
  public static void modelToModelWithMPC() {
    final Random random = new Random();
    final VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
    InputOutput.<String>println(videoGen.getInformation().getAuthorName());
    final Consumer<Media> _function = (Media video) -> {
      try {
        if ((video instanceof MandatoryVideoSeq)) {
          final VideoDescription desc = ((MandatoryVideoSeq)video).getDescription();
          String _videoid = desc.getVideoid();
          String _plus = ("Playing..." + _videoid);
          InputOutput.<String>println(_plus);
          Runtime _runtime = Runtime.getRuntime();
          String _location = desc.getLocation();
          String _plus_1 = (VideoGenTp2.player + _location);
          String _plus_2 = (_plus_1 + "");
          Process p = _runtime.exec(_plus_2);
          p.waitFor();
        } else {
          if ((video instanceof OptionalVideoSeq)) {
            boolean _nextBoolean = random.nextBoolean();
            if (_nextBoolean) {
              final VideoDescription desc_1 = ((OptionalVideoSeq)video).getDescription();
              String _videoid_1 = desc_1.getVideoid();
              String _plus_3 = ("Playing..." + _videoid_1);
              InputOutput.<String>println(_plus_3);
              Runtime _runtime_1 = Runtime.getRuntime();
              String _location_1 = desc_1.getLocation();
              String _plus_4 = (VideoGenTp2.player + _location_1);
              String _plus_5 = (_plus_4 + "");
              Process p_1 = _runtime_1.exec(_plus_5);
              p_1.waitFor();
            }
          } else {
            if ((video instanceof AlternativeVideoSeq)) {
              final VideoDescription videoSelected = ((AlternativeVideoSeq)video).getVideodescs().get(random.nextInt(((AlternativeVideoSeq)video).getVideodescs().size()));
              String _videoid_2 = videoSelected.getVideoid();
              String _plus_6 = ("Playing..." + _videoid_2);
              InputOutput.<String>println(_plus_6);
              Runtime _runtime_2 = Runtime.getRuntime();
              String _location_2 = videoSelected.getLocation();
              String _plus_7 = (VideoGenTp2.player + _location_2);
              String _plus_8 = (_plus_7 + "");
              Process p_2 = _runtime_2.exec(_plus_8);
              p_2.waitFor();
            }
          }
        }
      } catch (Throwable _e) {
        throw Exceptions.sneakyThrow(_e);
      }
    };
    videoGen.getMedias().forEach(_function);
  }
  
  /**
   * Q2
   */
  public static void modelToTextWithFfmpeg(final VideoGenHelper videoGen) {
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
        (("cmd /c start cmd.exe " + "/K \" ffmpeg -f concat -safe 0 -i playlistFfmpeg.txt -c copy output_concat.mp4 ") + "&& exit\""));
      p.waitFor();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Q3
   */
  public static int durationMaxOfOneSequence(final VideoGenHelper videoGen) {
    int durationMax = 0;
    int durationMaxOfAlternativeVideo = 0;
    VideoGeneratorModel videogen = videoGen.loadVideoGenerator(URI.createURI("example1.videogen"));
    final EList<Media> videos = videogen.getMedias();
    for (final Media video : videos) {
      if ((video instanceof MandatoryVideoSeq)) {
        int duration = VideoGenTp2.durationOfVideo(((MandatoryVideoSeq)video).getDescription().getLocation());
        VideoDescription _description = ((MandatoryVideoSeq)video).getDescription();
        _description.setDuration(duration);
        durationMax = (durationMax + duration);
      } else {
        if ((video instanceof OptionalVideoSeq)) {
          final int duration_1 = VideoGenTp2.durationOfVideo(((OptionalVideoSeq)video).getDescription().getLocation());
          VideoDescription _description_1 = ((OptionalVideoSeq)video).getDescription();
          _description_1.setDuration(duration_1);
          durationMax = (durationMax + duration_1);
        } else {
          if ((video instanceof AlternativeVideoSeq)) {
            EList<VideoDescription> _videodescs = ((AlternativeVideoSeq)video).getVideodescs();
            for (final VideoDescription videodesc : _videodescs) {
              {
                final int duration_2 = VideoGenTp2.durationOfVideo(videodesc.getLocation());
                videodesc.setDuration(duration_2);
                if ((durationMaxOfAlternativeVideo < duration_2)) {
                  durationMaxOfAlternativeVideo = duration_2;
                }
              }
            }
            durationMax = (durationMax + durationMaxOfAlternativeVideo);
          }
        }
      }
    }
    InputOutput.<String>println((("durationMax : " + Integer.valueOf(durationMax)) + " secondes"));
    return durationMax;
  }
  
  /**
   * For Q3
   */
  public static int durationOfVideo(final String videoPath) {
    try {
      final Process process = Runtime.getRuntime().exec(
        ("ffprobe -v error -select_streams v:0 -show_entries stream=duration -of default=noprint_wrappers=1:nokey=1 -i " + videoPath));
      process.waitFor();
      InputStream _inputStream = process.getInputStream();
      InputStreamReader _inputStreamReader = new InputStreamReader(_inputStream);
      BufferedReader reader = new BufferedReader(_inputStreamReader);
      final String outputCmd = reader.readLine();
      final int duration = Math.round(Float.parseFloat(outputCmd));
      InputOutput.<String>println((((("duration of " + videoPath) + " : ") + Integer.valueOf(duration)) + " secondes"));
      return duration;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Q4
   */
  public static void generateFramesForEachVideo(final VideoGenHelper videoGen, final String outputFolder) {
    try {
      VideoGeneratorModel videogen = videoGen.loadVideoGenerator(URI.createURI("example1.videogen"));
      final EList<Media> videos = videogen.getMedias();
      for (final Media video : videos) {
        if ((video instanceof MandatoryVideoSeq)) {
          Runtime _runtime = Runtime.getRuntime();
          String _location = ((MandatoryVideoSeq)video).getDescription().getLocation();
          String _plus = ("ffmpeg -y -i " + _location);
          String _plus_1 = (_plus + " -r 1 -t 00:00:01 -ss 00:00:02 ");
          String _plus_2 = (_plus_1 + outputFolder);
          String _videoid = ((MandatoryVideoSeq)video).getDescription().getVideoid();
          String _plus_3 = (_plus_2 + _videoid);
          String _plus_4 = (_plus_3 + "-mandatory.jpg");
          final Process process = _runtime.exec(_plus_4);
          process.waitFor();
          String _videoid_1 = ((MandatoryVideoSeq)video).getDescription().getVideoid();
          String _plus_5 = ("frame generated : " + _videoid_1);
          String _plus_6 = (_plus_5 + "-mandatory.jpg");
          InputOutput.<String>println(_plus_6);
        } else {
          if ((video instanceof OptionalVideoSeq)) {
            Runtime _runtime_1 = Runtime.getRuntime();
            String _location_1 = ((OptionalVideoSeq)video).getDescription().getLocation();
            String _plus_7 = ("ffmpeg -y -i " + _location_1);
            String _plus_8 = (_plus_7 + " -r 1 -t 00:00:01 -ss 00:00:02 ");
            String _plus_9 = (_plus_8 + outputFolder);
            String _videoid_2 = ((OptionalVideoSeq)video).getDescription().getVideoid();
            String _plus_10 = (_plus_9 + _videoid_2);
            String _plus_11 = (_plus_10 + "-optional.jpg");
            final Process process_1 = _runtime_1.exec(_plus_11);
            process_1.waitFor();
            String _videoid_3 = ((OptionalVideoSeq)video).getDescription().getVideoid();
            String _plus_12 = ("frame generated : " + _videoid_3);
            String _plus_13 = (_plus_12 + "-optional.jpg");
            InputOutput.<String>println(_plus_13);
          } else {
            if ((video instanceof AlternativeVideoSeq)) {
              EList<VideoDescription> _videodescs = ((AlternativeVideoSeq)video).getVideodescs();
              for (final VideoDescription videodesc : _videodescs) {
                {
                  Runtime _runtime_2 = Runtime.getRuntime();
                  String _location_2 = videodesc.getLocation();
                  String _plus_14 = ("ffmpeg -y -i " + _location_2);
                  String _plus_15 = (_plus_14 + " -r 1 -t 00:00:01 -ss 00:00:02 ");
                  String _plus_16 = (_plus_15 + outputFolder);
                  String _videoid_4 = videodesc.getVideoid();
                  String _plus_17 = (_plus_16 + _videoid_4);
                  String _plus_18 = (_plus_17 + "-alternative.jpg");
                  final Process process_2 = _runtime_2.exec(_plus_18);
                  process_2.waitFor();
                  String _videoid_5 = videodesc.getVideoid();
                  String _plus_19 = ("frame generated : " + _videoid_5);
                  String _plus_20 = (_plus_19 + "-alternative.jpg");
                  InputOutput.<String>println(_plus_20);
                }
              }
            }
          }
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Q5
   */
  public static void generateHtmlWithFrames(final VideoGenHelper videoGen, final String outputFolder) {
    try {
      final FileWriter file = new FileWriter("generateFrames.html");
      String framesMandatory = "";
      String framesOptional = "";
      String framesAlternative = "";
      VideoGeneratorModel videogen = videoGen.loadVideoGenerator(URI.createURI("example1.videogen"));
      final EList<Media> videos = videogen.getMedias();
      for (final Media video : videos) {
        if ((video instanceof MandatoryVideoSeq)) {
          Runtime _runtime = Runtime.getRuntime();
          String _location = ((MandatoryVideoSeq)video).getDescription().getLocation();
          String _plus = ("ffmpeg -y -i " + _location);
          String _plus_1 = (_plus + " -r 1 -t 00:00:01 -ss 00:00:02 ");
          String _plus_2 = (_plus_1 + outputFolder);
          String _videoid = ((MandatoryVideoSeq)video).getDescription().getVideoid();
          String _plus_3 = (_plus_2 + _videoid);
          String _plus_4 = (_plus_3 + "-mandatory.jpg");
          final Process process = _runtime.exec(_plus_4);
          process.waitFor();
          String _videoid_1 = ((MandatoryVideoSeq)video).getDescription().getVideoid();
          String _plus_5 = ("frame generated : " + _videoid_1);
          String _plus_6 = (_plus_5 + "-mandatory.jpg");
          InputOutput.<String>println(_plus_6);
          String _framesMandatory = framesMandatory;
          String _videoid_2 = ((MandatoryVideoSeq)video).getDescription().getVideoid();
          String _plus_7 = (("<p><b>Mandatory</b></p>\n\r\n\t\t\t\t\t<img src = " + outputFolder) + _videoid_2);
          String _plus_8 = (_plus_7 + "-mandatory.jpg");
          String _plus_9 = (_plus_8 + " width=\'300px\' height=auto/><br/>\n");
          framesMandatory = (_framesMandatory + _plus_9);
        } else {
          if ((video instanceof OptionalVideoSeq)) {
            Runtime _runtime_1 = Runtime.getRuntime();
            String _location_1 = ((OptionalVideoSeq)video).getDescription().getLocation();
            String _plus_10 = ("ffmpeg -y -i " + _location_1);
            String _plus_11 = (_plus_10 + " -r 1 -t 00:00:01 -ss 00:00:02 ");
            String _plus_12 = (_plus_11 + outputFolder);
            String _videoid_3 = ((OptionalVideoSeq)video).getDescription().getVideoid();
            String _plus_13 = (_plus_12 + _videoid_3);
            String _plus_14 = (_plus_13 + "-optional.jpg");
            final Process process_1 = _runtime_1.exec(_plus_14);
            process_1.waitFor();
            String _videoid_4 = ((OptionalVideoSeq)video).getDescription().getVideoid();
            String _plus_15 = ("frame generated : " + _videoid_4);
            String _plus_16 = (_plus_15 + "-optional.jpg");
            InputOutput.<String>println(_plus_16);
            String _framesOptional = framesOptional;
            String _videoid_5 = ((OptionalVideoSeq)video).getDescription().getVideoid();
            String _plus_17 = (("<p><b>Optional</b></p>\n\r\n\t\t\t\t\t<img src = " + outputFolder) + _videoid_5);
            String _plus_18 = (_plus_17 + "-optional.jpg");
            String _plus_19 = (_plus_18 + " width=\'300px\' height=auto/><br/>\n");
            framesOptional = (_framesOptional + _plus_19);
          } else {
            if ((video instanceof AlternativeVideoSeq)) {
              String _framesAlternative = framesAlternative;
              framesAlternative = (_framesAlternative + "<p><b>Alternatives</b></p>\n");
              EList<VideoDescription> _videodescs = ((AlternativeVideoSeq)video).getVideodescs();
              for (final VideoDescription videodesc : _videodescs) {
                {
                  Runtime _runtime_2 = Runtime.getRuntime();
                  String _location_2 = videodesc.getLocation();
                  String _plus_20 = ("ffmpeg -y -i " + _location_2);
                  String _plus_21 = (_plus_20 + " -r 1 -t 00:00:01 -ss 00:00:02 ");
                  String _plus_22 = (_plus_21 + outputFolder);
                  String _videoid_6 = videodesc.getVideoid();
                  String _plus_23 = (_plus_22 + _videoid_6);
                  String _plus_24 = (_plus_23 + "-alternative.jpg");
                  final Process process_2 = _runtime_2.exec(_plus_24);
                  process_2.waitFor();
                  String _videoid_7 = videodesc.getVideoid();
                  String _plus_25 = ("frame generated : " + _videoid_7);
                  String _plus_26 = (_plus_25 + "-alternative.jpg");
                  InputOutput.<String>println(_plus_26);
                  String _framesAlternative_1 = framesAlternative;
                  String _videoid_8 = videodesc.getVideoid();
                  String _plus_27 = (("<img src = " + outputFolder) + _videoid_8);
                  String _plus_28 = (_plus_27 + "-alternative.jpg");
                  String _plus_29 = (_plus_28 + " width=\'300\' height=auto/><br/>\n");
                  framesAlternative = (_framesAlternative_1 + _plus_29);
                }
              }
            }
          }
        }
      }
      file.write(
        (((((((("<!DOCTYPE html>\n" + "<head>\n") + "</head>\n") + "<body>\n") + framesMandatory) + framesOptional) + framesAlternative) + "</body>") + "</html>"));
      file.close();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
