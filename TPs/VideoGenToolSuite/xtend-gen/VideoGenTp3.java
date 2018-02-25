import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
 * TP3
 */
@SuppressWarnings("all")
public class VideoGenTp3 {
  public static void main(final String[] args) {
    VideoGenHelper _videoGenHelper = new VideoGenHelper();
    VideoGenTp3.generateCsv(_videoGenHelper);
  }
  
  /**
   * Q1
   */
  public static void generateCsv(final VideoGenHelper videoGen) {
    try {
      String content = "";
      VideoGeneratorModel videogen = videoGen.loadVideoGenerator(URI.createURI("example1.videogen"));
      LinkedList<LinkedList<VideoDescription>> listVariantVid = new LinkedList<LinkedList<VideoDescription>>();
      String _content = content;
      content = (_content + "id;");
      final EList<Media> videos = videogen.getMedias();
      List<String> nameColumns = new LinkedList<String>();
      for (final Media video : videos) {
        if ((video instanceof MandatoryVideoSeq)) {
          nameColumns.add(((MandatoryVideoSeq)video).getDescription().getVideoid());
          String _content_1 = content;
          String _videoid = ((MandatoryVideoSeq)video).getDescription().getVideoid();
          String _plus = (_videoid + ";");
          content = (_content_1 + _plus);
        } else {
          if ((video instanceof OptionalVideoSeq)) {
            nameColumns.add(((OptionalVideoSeq)video).getDescription().getVideoid());
            String _content_2 = content;
            String _videoid_1 = ((OptionalVideoSeq)video).getDescription().getVideoid();
            String _plus_1 = (_videoid_1 + ";");
            content = (_content_2 + _plus_1);
          } else {
            if ((video instanceof AlternativeVideoSeq)) {
              EList<VideoDescription> _videodescs = ((AlternativeVideoSeq)video).getVideodescs();
              for (final VideoDescription videodesc : _videodescs) {
                {
                  nameColumns.add(videodesc.getVideoid());
                  String _content_3 = content;
                  String _videoid_2 = videodesc.getVideoid();
                  String _plus_2 = (_videoid_2 + ";");
                  content = (_content_3 + _plus_2);
                }
              }
            }
          }
        }
      }
      String _content_3 = content;
      content = (_content_3 + "size\n");
      int ii = 1;
      for (final LinkedList<VideoDescription> listVariantVideos : listVariantVid) {
        {
          int size = 0;
          String _content_4 = content;
          String _plus_2 = (Integer.valueOf(ii) + ";");
          content = (_content_4 + _plus_2);
          List<String> listName = new ArrayList<String>();
          for (int i = 0; (i < listVariantVideos.size()); i++) {
            {
              VideoDescription desc = listVariantVideos.get(i);
              String _location = desc.getLocation();
              final File currentFile = new File(_location);
              int _size = size;
              long _length = currentFile.length();
              size = (_size + ((int) _length));
              listName.add(listVariantVideos.get(i).getVideoid());
            }
          }
          for (final String name : nameColumns) {
            boolean _contains = listName.contains(name);
            if (_contains) {
              String _content_5 = content;
              content = (_content_5 + "TRUE;");
            } else {
              String _content_6 = content;
              content = (_content_6 + "FALSE;");
            }
          }
          String _content_7 = content;
          String _plus_3 = (Integer.valueOf(size) + "\n");
          content = (_content_7 + _plus_3);
          ii++;
        }
      }
      final FileWriter file = new FileWriter("videos-size.csv");
      file.write(content);
      file.close();
      InputOutput.<String>println(("content cvs : " + content));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * For Q1
   */
  public static ArrayList<ArrayList<VideoDescription>> listVariantVideos(final VideoGeneratorModel videoGen) {
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
   * Q2 : d'après R la valeur de corrélation est de 1 donc forte corrélation
   * et donc quand on concatène les vidéos ça revient au même poids fichier que les vidéos séparées
   */
  public static void generateCsvWithRealSize() {
    try {
      String content = "";
      Process p = Runtime.getRuntime().exec(
        (("cmd /c start cmd.exe " + "/K \" ffmpeg -f concat -safe 0 -i playlist1.txt -c copy variantesVideo/variante1.mp4 ") + "&& exit\""));
      p.waitFor();
      Process p2 = Runtime.getRuntime().exec(
        (("cmd /c start cmd.exe " + "/K \" ffmpeg -f concat -safe 0 -i playlist2.txt -c copy variantesVideo/variante2.mp4 ") + "&& exit\""));
      p2.waitFor();
      Process p3 = Runtime.getRuntime().exec(
        (("cmd /c start cmd.exe " + "/K \" ffmpeg -f concat -safe 0 -i playlist3.txt -c copy variantesVideo/variante3.mp4 ") + "&& exit\""));
      p3.waitFor();
      Process p4 = Runtime.getRuntime().exec(
        (("cmd /c start cmd.exe " + "/K \" ffmpeg -f concat -safe 0 -i playlist4.txt -c copy variantesVideo/variante4.mp4 ") + "&& exit\""));
      p4.waitFor();
      FileReader _fileReader = new FileReader("videos-size.csv");
      BufferedReader br = new BufferedReader(_fileReader);
      String line = br.readLine();
      String _content = content;
      content = (_content + line);
      String _content_1 = content;
      content = (_content_1 + ",realSize\n");
      int i = 1;
      while (((line = br.readLine()) != null)) {
        {
          final File videoVariante = new File((("variantesVideo/variante" + Integer.valueOf(i)) + ".mp4"));
          i++;
          String _content_2 = content;
          content = (_content_2 + line);
          String _content_3 = content;
          long _length = videoVariante.length();
          String _plus = ("," + Long.valueOf(_length));
          String _plus_1 = (_plus + "\n");
          content = (_content_3 + _plus_1);
        }
      }
      final FileWriter file = new FileWriter("videos-realSize.csv");
      file.write(content);
      file.close();
      InputOutput.<String>println(content);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Q3
   */
  public static void gification(final int start, final int duration, final int size, final File video, final String outputFolder) {
    try {
      Runtime _runtime = Runtime.getRuntime();
      String _name = video.getName();
      String _plus = (((((("cmd /c start cmd.exe " + "/K \"ffmpeg -y -ss ") + Integer.valueOf(start)) + " -t ") + Integer.valueOf(duration)) + " -i variantesVideo/") + _name);
      String _plus_1 = (_plus + " -vf fps=10,scale=");
      String _plus_2 = (_plus_1 + Integer.valueOf(size));
      String _plus_3 = (_plus_2 + ":-1:flags=lanczos,palettegen ");
      String _plus_4 = (_plus_3 + outputFolder);
      String _name_1 = video.getName();
      String _plus_5 = (_plus_4 + _name_1);
      String _plus_6 = (_plus_5 + "-palette.png");
      String _plus_7 = (_plus_6 + "&& ffmpeg -ss ");
      String _plus_8 = (_plus_7 + Integer.valueOf(start));
      String _plus_9 = (_plus_8 + " -t ");
      String _plus_10 = (_plus_9 + Integer.valueOf(duration));
      String _plus_11 = (_plus_10 + " -i variantesVideo/");
      String _name_2 = video.getName();
      String _plus_12 = (_plus_11 + _name_2);
      String _plus_13 = (_plus_12 + " -i gif/");
      String _name_3 = video.getName();
      String _plus_14 = (_plus_13 + _name_3);
      String _plus_15 = (_plus_14 + "-palette.png -filter_complex \"fps=10,scale=");
      String _plus_16 = (_plus_15 + Integer.valueOf(size));
      String _plus_17 = (_plus_16 + ":-1:flags=lanczos[x];[x][1:v]paletteuse\" ");
      String _plus_18 = (_plus_17 + outputFolder);
      String _name_4 = video.getName();
      String _plus_19 = (_plus_18 + _name_4);
      String _plus_20 = (_plus_19 + ".gif");
      String _plus_21 = (_plus_20 + "&& exit\"");
      final Process process1 = _runtime.exec(_plus_21);
      process1.waitFor();
      String _name_5 = video.getName();
      String _plus_22 = (("palette generated : " + outputFolder) + _name_5);
      String _plus_23 = (_plus_22 + "-palette.png");
      InputOutput.<String>println(_plus_23);
      String _name_6 = video.getName();
      String _plus_24 = (("gif generated : " + outputFolder) + _name_6);
      String _plus_25 = (_plus_24 + ".gif");
      InputOutput.<String>println(_plus_25);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * For Q4 : logiquement la taille du gif dépend de la durée définie, donc osef de la comparasion des tailles
   */
  public static long gifSize(final int start, final int duration, final int size, final File video, final String outputFolder) {
    try {
      Runtime _runtime = Runtime.getRuntime();
      String _name = video.getName();
      String _plus = (((((("cmd /c start cmd.exe " + "/K \"ffmpeg -y -ss ") + Integer.valueOf(start)) + " -t ") + Integer.valueOf(duration)) + " -i variantesVideo/") + _name);
      String _plus_1 = (_plus + " -vf fps=10,scale=");
      String _plus_2 = (_plus_1 + Integer.valueOf(size));
      String _plus_3 = (_plus_2 + ":-1:flags=lanczos,palettegen ");
      String _plus_4 = (_plus_3 + outputFolder);
      String _name_1 = video.getName();
      String _plus_5 = (_plus_4 + _name_1);
      String _plus_6 = (_plus_5 + "-palette.png");
      String _plus_7 = (_plus_6 + "&& ffmpeg -ss ");
      String _plus_8 = (_plus_7 + Integer.valueOf(start));
      String _plus_9 = (_plus_8 + " -t ");
      String _plus_10 = (_plus_9 + Integer.valueOf(duration));
      String _plus_11 = (_plus_10 + " -i variantesVideo/");
      String _name_2 = video.getName();
      String _plus_12 = (_plus_11 + _name_2);
      String _plus_13 = (_plus_12 + " -i gif/");
      String _name_3 = video.getName();
      String _plus_14 = (_plus_13 + _name_3);
      String _plus_15 = (_plus_14 + "-palette.png -filter_complex \"fps=10,scale=");
      String _plus_16 = (_plus_15 + Integer.valueOf(size));
      String _plus_17 = (_plus_16 + ":-1:flags=lanczos[x];[x][1:v]paletteuse\" ");
      String _plus_18 = (_plus_17 + outputFolder);
      String _name_4 = video.getName();
      String _plus_19 = (_plus_18 + _name_4);
      String _plus_20 = (_plus_19 + ".gif");
      String _plus_21 = (_plus_20 + "&& exit\"");
      final Process process1 = _runtime.exec(_plus_21);
      process1.waitFor();
      String _name_5 = video.getName();
      String _plus_22 = (("palette generated : " + outputFolder) + _name_5);
      String _plus_23 = (_plus_22 + "-palette.png");
      InputOutput.<String>println(_plus_23);
      String _name_6 = video.getName();
      String _plus_24 = (("gif generated : " + outputFolder) + _name_6);
      String _plus_25 = (_plus_24 + ".gif");
      InputOutput.<String>println(_plus_25);
      String _name_7 = video.getName();
      String _plus_26 = (outputFolder + _name_7);
      String _plus_27 = (_plus_26 + ".gif");
      final File sizeFileGif = new File(_plus_27);
      long _length = sizeFileGif.length();
      String _plus_28 = ("size of gif : " + Long.valueOf(_length));
      String _plus_29 = (_plus_28 + " bytes");
      InputOutput.<String>println(_plus_29);
      return sizeFileGif.length();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
