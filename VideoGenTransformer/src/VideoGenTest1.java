import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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


	public List<List<String>> genVariantesFromVideoGeneratorModel() {

		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
		assertNotNull(videoGen);

		List<String> op_list = new ArrayList<>();
		List<String> man_list = new ArrayList<>();
		List<String> alt_list = new ArrayList<>();
		List<List<String>> op_man_alt_list = new ArrayList<List<String>>();

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
					man_list.add(vidDesc.getLocation());
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
					op_list.add(vidDesc.getLocation());
					System.out.println(vidDesc.getLocation() + "  " + f.length());
				}

			}
			else {
				AlternativesMedia alternativesMed = (AlternativesMedia) m ;
				for(MediaDescription mDesc : alternativesMed.getMedias()){
					if (mDesc instanceof VideoDescription){
						VideoDescription vidDesc = (VideoDescription) mDesc;
						File f = new File(vidDesc.getLocation());
						alt_list.add(vidDesc.getLocation());
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

		List<List<String>> combin_op_list = allCombinations(op_list);
		op_man_alt_list = listVariants(combin_op_list,alt_list,man_list);
		return op_man_alt_list;

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

	private void execEcho(String name, int index) {
		String s;
		Process p;
		try {

			String [] commands = { "bash", "-c", "echo \"file " + "\'$PWD/playList/Videos/" + name + "\'"+ "\" >> playList/variantes/variante" + index + ".txt" };
			p = Runtime.getRuntime().exec(commands);

			BufferedReader br = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			while ((s = br.readLine()) != null)
				System.out.println("line: " + s);
			p.waitFor();
			//System.out.println ("exit echo: " + p.exitValue());
			p.destroy();
		} catch (Exception e) {}
	}
	private void execFFMPEG(String typeCommand, String videoName) {
		String cmd ="";
		switch(typeCommand){
		case "video":
			cmd = "ffmpeg -safe 0 -f concat -segment_time_metadata 1 -i playList/variantes/"+videoName+".txt -vf select=concatdec_select -af aselect=concatdec_select,aresample=async=1 playList/VideosGenerated/"+videoName+".mp4";
			break;
		case "image":
			cmd = "ffmpeg -y -i playList/VideosGenerated/"+videoName+".mp4 -r 1 -t 00:00:03 -ss 00:00:03 -f image2 playList/Images/"+ videoName+".png";
			break;
		case "gif":
			cmd = "ffmpeg -ss 3.0 -t 7.0 -i playList/VideosGenerated/"+videoName+".mp4 -f gif playList/Gif/"+videoName+".gif";
			break;
		default:
			throw new IllegalArgumentException("Type of command must be : video, image or gif");
		}
		try {
			System.out.println(cmd);
			String[] a = new String[] {"/bin/sh", "-c", cmd};
			Process p = Runtime.getRuntime().exec(a);
			p.waitFor();
			//System.out.println ("exit execFFMPEG: " + p.exitValue());
			p.destroy();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public File[] finder( String dirName){
		File dir = new File(dirName);

		return dir.listFiles(new FilenameFilter() { 
			public boolean accept(File dir, String filename)
			{ return filename.endsWith(".txt"); }
		} );

	}

	private void generateAllVideosImagesGifs() {
		File [] playlists = finder("playList/variantes/");
		for (File f : playlists) {
			String nameFile = f.getName().split("\\.(?=[^\\.]+$)")[0];
			//System.out.println("-----------------Exec FFMEG : "+nameFile+" --------------");
			execFFMPEG("video", nameFile);
			execFFMPEG("image", nameFile);
			execFFMPEG("gif", nameFile);
		}
	}
	
	private void generateData() {
		int i = 0;
		for (List<String> l : genVariantesFromVideoGeneratorModel() ) {
			i++;

			System.out.println(l);

			for(String video : l) {
				execEcho(video, i);
			}
		}
		generateAllVideosImagesGifs();
	}
	
	
	
	public static void main(String[] args) throws InterruptedException {
		VideoGenTest1 v = new VideoGenTest1();
		v.generateData();
	}



}


