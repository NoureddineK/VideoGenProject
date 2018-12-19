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
			"echo \"file " + file + "\" >> git/teaching-MDE-IL1819/VideoGenTransformer/mylist" + i + ".txt"
		);
		print.waitFor;
		println("echo \"file " + file + "\">> mylist" + i + ".txt");
	}

	@Test
	def void testLoadModel() {
		var cmd = "echo \"file '";
		var file = "'\" >> git/teaching-MDE-IL1819/VideoGenTransformer/mylist";
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
								println(mon);
								var print = Runtime.runtime.exec(mon);
								if (opt != "") {
									println(opt);
									print = Runtime.runtime.exec(opt);
								}
								println(cmd + mediaDesc.location + file + i + txt);
								print = Runtime.runtime.exec(cmd + mediaDesc.location + file + i + txt);
								print.waitFor;
								i++;
							} else {
								if (mediaDesc instanceof ImageDescription) {
									// println(mediaDesc.location);
									// echo(mediaDesc.location, i);
									// var print = Runtime.getRuntime.exec(cmd + mediaDesc.location + file + i + txt);
									// print.waitFor;
									println(mon);
									if (opt != "") {
										println(opt);
										print = Runtime.runtime.exec(opt);
									}
									println(cmd + mediaDesc.location + file + i + txt);
									var print = Runtime.runtime.exec(mon);
									print = Runtime.runtime.exec(opt);
									print = Runtime.runtime.exec(cmd + mediaDesc.location + file + i + txt);
									print.waitFor;
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
