import com.google.common.base.Objects;
import fr.istic.videoGen.AlternativesMedia;
import fr.istic.videoGen.ImageDescription;
import fr.istic.videoGen.MandatoryMedia;
import fr.istic.videoGen.Media;
import fr.istic.videoGen.MediaDescription;
import fr.istic.videoGen.OptionalMedia;
import fr.istic.videoGen.VideoDescription;
import fr.istic.videoGen.VideoGeneratorModel;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class VideoGenTest1XtendVersion {
  public String echo(final String file, final int i) {
    try {
      String _xblockexpression = null;
      {
        Process print = Runtime.getRuntime().exec(
          (((("echo \"file " + file) + "\" >> git/teaching-MDE-IL1819/VideoGenTransformer/mylist") + Integer.valueOf(i)) + ".txt"));
        print.waitFor();
        _xblockexpression = InputOutput.<String>println((((("echo \"file " + file) + "\">> mylist") + Integer.valueOf(i)) + ".txt"));
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void testLoadModel() {
    try {
      String cmd = "echo \"file \'";
      String file = "\'\" >> git/teaching-MDE-IL1819/VideoGenTransformer/mylist";
      String txt = ".txt";
      String mon = "";
      String opt = "";
      int i = 0;
      final VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
      Assert.assertNotNull(videoGen);
      EList<Media> _medias = videoGen.getMedias();
      for (final Media media : _medias) {
        if ((media instanceof MandatoryMedia)) {
          String _location = ((MandatoryMedia)media).getDescription().getLocation();
          String _plus = (cmd + _location);
          String _plus_1 = (_plus + file);
          String _plus_2 = (_plus_1 + Integer.valueOf(i));
          String _plus_3 = (_plus_2 + txt);
          mon = _plus_3;
        } else {
          if ((media instanceof OptionalMedia)) {
            boolean _randomBoolean = VideoGenHelper.getRandomBoolean();
            if (_randomBoolean) {
              String _location_1 = ((OptionalMedia)media).getDescription().getLocation();
              String _plus_4 = (cmd + _location_1);
              String _plus_5 = (_plus_4 + file);
              String _plus_6 = (_plus_5 + Integer.valueOf(i));
              String _plus_7 = (_plus_6 + txt);
              opt = _plus_7;
            } else {
              opt = "";
            }
          } else {
            if ((media instanceof AlternativesMedia)) {
              EList<MediaDescription> _medias_1 = ((AlternativesMedia)media).getMedias();
              for (final MediaDescription mediaDesc : _medias_1) {
                if ((mediaDesc instanceof VideoDescription)) {
                  InputOutput.<String>println(mon);
                  Process print = Runtime.getRuntime().exec(mon);
                  boolean _notEquals = (!Objects.equal(opt, ""));
                  if (_notEquals) {
                    InputOutput.<String>println(opt);
                    print = Runtime.getRuntime().exec(opt);
                  }
                  String _location_2 = ((VideoDescription)mediaDesc).getLocation();
                  String _plus_8 = (cmd + _location_2);
                  String _plus_9 = (_plus_8 + file);
                  String _plus_10 = (_plus_9 + Integer.valueOf(i));
                  String _plus_11 = (_plus_10 + txt);
                  InputOutput.<String>println(_plus_11);
                  Runtime _runtime = Runtime.getRuntime();
                  String _location_3 = ((VideoDescription)mediaDesc).getLocation();
                  String _plus_12 = (cmd + _location_3);
                  String _plus_13 = (_plus_12 + file);
                  String _plus_14 = (_plus_13 + Integer.valueOf(i));
                  String _plus_15 = (_plus_14 + txt);
                  print = _runtime.exec(_plus_15);
                  print.waitFor();
                  i++;
                } else {
                  if ((mediaDesc instanceof ImageDescription)) {
                    InputOutput.<String>println(mon);
                    boolean _notEquals_1 = (!Objects.equal(opt, ""));
                    if (_notEquals_1) {
                      InputOutput.<String>println(opt);
                      InputOutput.print(Runtime.getRuntime().exec(opt));
                    }
                    String _location_4 = ((ImageDescription)mediaDesc).getLocation();
                    String _plus_16 = (cmd + _location_4);
                    String _plus_17 = (_plus_16 + file);
                    String _plus_18 = (_plus_17 + Integer.valueOf(i));
                    String _plus_19 = (_plus_18 + txt);
                    InputOutput.<String>println(_plus_19);
                    Process print_1 = Runtime.getRuntime().exec(mon);
                    print_1 = Runtime.getRuntime().exec(opt);
                    Runtime _runtime_1 = Runtime.getRuntime();
                    String _location_5 = ((ImageDescription)mediaDesc).getLocation();
                    String _plus_20 = (cmd + _location_5);
                    String _plus_21 = (_plus_20 + file);
                    String _plus_22 = (_plus_21 + Integer.valueOf(i));
                    String _plus_23 = (_plus_22 + txt);
                    print_1 = _runtime_1.exec(_plus_23);
                    print_1.waitFor();
                    i++;
                  }
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
}
