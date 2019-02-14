import fr.istic.videoGen.AlternativesMedia
import fr.istic.videoGen.ImageDescription
import fr.istic.videoGen.MandatoryMedia
import fr.istic.videoGen.Media
import fr.istic.videoGen.MediaDescription
import fr.istic.videoGen.OptionalMedia
import fr.istic.videoGen.VideoDescription
import org.eclipse.emf.common.util.URI
import org.junit.Test

import static org.junit.Assert.*

class VideoGenTest1XtendVersion {

	def echo(String file, int i) {
		var print = Runtime.runtime.exec(
			"echo \"file " + file + "\" >> mylist" + i + ".txt");
		print.waitFor;
		println("echo \"file " + file + "\">> mylist" + i + ".txt");
	}

@Test
def void test1(){

	val char[] arr = #["A", "B", "C", "D"];
	possibleStrings(5, arr, "");
}

def possibleStrings(int maxLength, char[] alphabet, String curr) throws Exception{

        // If the current string has reached it's maximum length
        if(curr.length() == maxLength) {
            System.out.println(curr);

        // Else add each letter from the alphabet to new strings and process these new strings again
        } else {
            for(var i = 0; i < alphabet.length; i++) {
                var oldCurr = curr;
                   System.out.println("Old: "+curr); 
                curr.concat(alphabet.get(i).toString);
                  System.out.println("i: "+curr);       
                possibleStrings(maxLength,alphabet,curr);
                curr.replace(curr,oldCurr.toString);
                  System.out.println("replace: "+curr);             
                
            }
        }
    }

	@Test
	def void testLoadModel() {
		var cmd = "echo \"file '";
		var file = "'\" >> mylist";
		var txt = ".txt";
		var mon = "";
		var opt = "";
		var i = 0;
		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"))
		assertNotNull(videoGen)

		for (Media media : videoGen.medias) {
			// MandatoryMedia
			if (media instanceof MandatoryMedia) {
				// println(media.description.location);
				// echo(media.description.location, i);
				// var print = Runtime.getRuntime.exec(cmd + media.description.location + file + i + txt);
				// print.waitFor;
				mon = cmd + media.description.location + file + i + txt;
			} else {
				// OptionalMedia
				if (media instanceof OptionalMedia) {
					if (VideoGenHelper.randomBoolean) {
						// println(media.description.location);
						// echo(media.description.location, i);
						// var print = Runtime.getRuntime.exec(cmd + media.description.location + file + i + txt);
						// print.waitFor;
						opt = cmd + media.description.location + file + i + txt;
					} else {
						opt = "";
					}
				} else {
					// AlternativesMedia
					if (media instanceof AlternativesMedia) {
						for (MediaDescription mediaDesc : media.medias) {

							if (mediaDesc instanceof VideoDescription) {
								// println(mediaDesc.location);
								// echo(mediaDesc.location, i);
								println("AlternativesMedia: "+mon);
								var printCmd = Runtime.runtime.exec(mon);
								if (opt != "") {
									println("opt: "+opt);
									printCmd = Runtime.runtime.exec(opt);
								}
								println("VideoDescription: " + cmd + mediaDesc.location + file + i + txt);

								//println(cmd + mediaDesc.location + file + i + txt);
								printCmd = Runtime.runtime.exec(cmd + mediaDesc.location + file + i + txt);
								printCmd.waitFor;
								i++;
							} else {
								if (mediaDesc instanceof ImageDescription) {
									// println(mediaDesc.location);
									// echo(mediaDesc.location, i);
									// var print = Runtime.getRuntime.exec(cmd + mediaDesc.location + file + i + txt);
									// print.waitFor;
									println(mon);
									if (opt != "") {
										println("opt Image: "+opt);
										print = Runtime.runtime.exec(opt);
									}
									println("ImageDescription: "+cmd + mediaDesc.location + file + i + txt);

									var printCmd = Runtime.runtime.exec(mon);
									printCmd = Runtime.runtime.exec(opt);
									printCmd = Runtime.runtime.exec(cmd + mediaDesc.location + file + i + txt);
									printCmd.waitFor;
									i++;
								}
							}
						}

					}
				}

			// and then visit the model
			// eg access video sequences: videoGen.videoseqs
			}
		}
	// var player = Runtime.runtime.exec("vlc " + videoSet);
	// player.waitFor
	}
}
