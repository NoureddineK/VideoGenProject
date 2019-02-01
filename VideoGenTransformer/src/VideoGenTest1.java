import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.junit.Test;

import fr.istic.videoGen.AlternativesMedia;
import fr.istic.videoGen.ImageDescription;
import fr.istic.videoGen.MandatoryMedia;
import fr.istic.videoGen.Media;
import fr.istic.videoGen.MediaDescription;
import fr.istic.videoGen.OptionalMedia;
import fr.istic.videoGen.VideoDescription;
import fr.istic.videoGen.VideoGeneratorModel;

public class VideoGenTest1 {
    
	
	public List<ArrayList<String>> allCombinations(List<String> optList) {
		List<ArrayList<String>> finalList = new ArrayList<ArrayList<String>>();
		//ArrayList<String> sousList = new ArrayList<String>();
		    int n = optList.size();
		    int N = (int) Math.pow(2d, Double.valueOf(n));  
		    for (int i = 1; i < N; i++) {
		    	ArrayList<String> sousList = new ArrayList<String>();
		        String code = Integer.toBinaryString(N | i).substring(1);
		        for (int j = 0; j < n; j++) {
		            if (code.charAt(j) == '1') {
		            	sousList.add(optList.get(j));   
		            	 System.out.print(optList.get(j));
		            }
		        }
		        System.out.println();
		        finalList.add(sousList);
		        //sousList.clear();
		    }
			return finalList;
	}
	
    @Test
    public void testInJava1() {
        
        VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
        assertNotNull(videoGen);
        List<String> optList = new ArrayList<String>();
        List<Media> medias = (videoGen.getMedias());
        for (Media m : medias ){
            if(m instanceof MandatoryMedia){
                MandatoryMedia mandatoryMed = (MandatoryMedia) m;
                if(mandatoryMed.getDescription() instanceof ImageDescription){
                    ImageDescription imDesc = (ImageDescription) mandatoryMed.getDescription() ;
                    File f = new File(imDesc.getLocation());
                    System.out.println(imDesc.getLocation()+ "  " + f.length());
                }else{
                    VideoDescription vidDesc = (VideoDescription) mandatoryMed.getDescription() ;
                    File f = new File(vidDesc.getLocation());
                    System.out.println(vidDesc.getLocation() + "  " + f.length());
                }
            }
            else if (m instanceof OptionalMedia){
            
                OptionalMedia optionalMed = (OptionalMedia) m;
             
                if (optionalMed.getDescription() instanceof ImageDescription){
                    ImageDescription imDesc = (ImageDescription) optionalMed.getDescription();
                    File f = new File(imDesc.getLocation());
                        System.out.println(imDesc.getLocation()+ "  " + f.length());
                }else{
                    VideoDescription vidDesc = (VideoDescription) optionalMed.getDescription();
                    File f = new File(vidDesc.getLocation());
                    System.out.println(vidDesc.getLocation() + "  " + f.length());
                }
                    
            }
            else {
                AlternativesMedia alternativesMed = (AlternativesMedia) m ;
                for(MediaDescription mDesc : alternativesMed.getMedias()){
                    if (mDesc instanceof VideoDescription){
                        VideoDescription vidDesc = (VideoDescription) mDesc;
                        File f = new File(vidDesc.getLocation());
                        System.out.println(vidDesc.getLocation()+ "  " + f.length());
                    }
                    else{
                        ImageDescription imDesc =(ImageDescription) mDesc;
                        File f = new File(imDesc.getLocation());
                        System.out.println(imDesc.getLocation()+ "  " + f.length());
                    }
                }
            }
        }
        
    }
    
    @Test
    public void testList() {
    	 List<String> optList = new ArrayList<String>();//{ "A", "B", "C", "D" };
		 optList.add("A");
		 optList.add("B");
		 optList.add("C");
		 optList.add("D");
		 List<ArrayList<String>> list =	allCombinations(optList);
		 for(ArrayList<String> l: list) {
			 System.out.println("List -> ");
			 for(String s: l) {
				 System.out.println("sousList: "+ s);
			 }
		 }
    }


}


