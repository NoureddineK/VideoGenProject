import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.URI;

import fr.istic.videoGen.AlternativesMedia;
import fr.istic.videoGen.ImageDescription;
import fr.istic.videoGen.MandatoryMedia;
import fr.istic.videoGen.Media;
import fr.istic.videoGen.MediaDescription;
import fr.istic.videoGen.OptionalMedia;
import fr.istic.videoGen.VideoDescription;
import fr.istic.videoGen.VideoGeneratorModel;

public class VideoGenTest1 {


	public void testInJava1() {

		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("specifications.videogen"));
		assertNotNull(videoGen);

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

	public List<List<String>> listVariants(List<List<String>> op, List<String> alt ,List<String> man) {
		List<List<String>> op_man = new ArrayList<List<String>>();
		for(List<String> listOpMan : op) {
			List<String> list_op_man =  new ArrayList<String>();
			list_op_man.addAll(listOpMan);
			list_op_man.addAll(man);
			op_man.add(list_op_man);
		}
		List<List<String>> op_man_alt = new ArrayList<List<String>>();
		int i_op_man = 0;
		int i_alt = 0;
		while(i_op_man < op_man.size()) {
			List<String> list_sans_alt = op_man.get(i_op_man);
			while(i_alt < alt.size()) {
				List<String> list_avec_alt = new ArrayList<>(list_sans_alt);
				String a = alt.get(i_alt);
				list_avec_alt.add(a);
				op_man_alt.add(list_avec_alt);
				i_alt++;
			}
			i_alt = 0;
			i_op_man++;
		}
		return op_man_alt;
	}
	public List<List<String>> allCombinations(List<String> optList) {
		List<List<String>> finalList = new ArrayList<List<String>>();
		// ajout de la combinaison vide
		List<String> sousListVide = new ArrayList<String>();
		finalList.add(sousListVide);
		int n = optList.size();
		int N = (int) Math.pow(2d, Double.valueOf(n));  
		for (int i = 1; i < N; i++) {
			ArrayList<String> sousList = new ArrayList<String>();
			String code = Integer.toBinaryString(N | i).substring(1);
			for (int j = 0; j < n; j++) {
				if (code.charAt(j) == '1') {
					sousList.add(optList.get(j));   
				}
			}
			finalList.add(sousList);
		}
		return finalList;
	}

	public void testList() {
		List<String> optList = new ArrayList<String>();
		optList.add("A");
		optList.add("B");
		optList.add("C");
		optList.add("D");
		List<List<String>> list =	allCombinations(optList);
		System.out.println(list.size());
	}

	public static void main(String[] args) {


		VideoGenTest1 v = new VideoGenTest1();
		List<String> man = Arrays.asList("man1", "man2", "man3");
		List<String> alt = Arrays.asList("alt1", "alt2", "alt3");
		List<String> op = Arrays.asList("op1","op2");
		List<List<String>> op_combinaisons = v.allCombinations(op);
		for (List<String> l : v.listVariants(op_combinaisons, alt, man) )
			System.out.println(l);
	}
    
	
	
}


