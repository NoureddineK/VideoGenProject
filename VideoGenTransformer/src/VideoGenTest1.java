import static org.junit.Assert.*;

import org.eclipse.emf.common.util.URI;
import org.junit.Test;

import fr.istic.videoGen.VideoGeneratorModel;

public class VideoGenTest1 {
	
	@Test
	public void testInJava1() {
		
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
		assertNotNull(videoGen);
		System.out.println(videoGen.getInformation().getAuthorName());		
		// and then visit the model
		// eg access video sequences;: 
		// videoGen.getVideoseqs();
		
	}


}



