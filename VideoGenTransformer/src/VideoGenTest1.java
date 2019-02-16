import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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

	private final String ADDIMAGE = "image";
	private final String ADDGIF = "gif";
	private final String ADDVIDEO = "video";
	private final String DURATION = "duration";

	/*
	 * Ces deux listes sont utilisees pour la creation du CSV
	 */
	HashMap<String, Integer> videoInfo = new HashMap<String, Integer>();
	HashMap<String, ArrayList<String>> playListInfo = new HashMap<String, ArrayList<String>>();

	/**
	 * Fonction qui calcule toutes les variantes possibles a partir de la grammaire videogen
	 * @return une liste de listes de variates  
	 */

	public List<List<String>> genVariantesFromVideoGeneratorModel() {
		VideoGeneratorModel videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"));
		assertNotNull(videoGen);
		List<String> op_list = new ArrayList<>();
		List<String> man_list = new ArrayList<>();
		List<String> alt_list = new ArrayList<>();
		List<List<String>> op_man_alt_list = new ArrayList<List<String>>();

		List<Media> medias = (videoGen.getMedias());
		for (Media m : medias) {
			if (m instanceof MandatoryMedia) {
				MandatoryMedia mandatoryMed = (MandatoryMedia) m;
				if(mandatoryMed.getDescription() instanceof ImageDescription){
					ImageDescription imDesc = (ImageDescription) mandatoryMed.getDescription() ;
					File f = new File("playList/Videos/"+imDesc.getLocation());
					System.out.println(imDesc.getLocation()+ "  " + f.length());
				}else{
					VideoDescription vidDesc = (VideoDescription) mandatoryMed.getDescription() ;
					File f = new File("playList/Videos/"+vidDesc.getLocation());
					man_list.add(vidDesc.getLocation());
					videoInfo.put(vidDesc.getLocation(), vidDesc.getDuration());
					System.out.println(vidDesc.getLocation() + "  " + f.length());
				}
			} else if (m instanceof OptionalMedia) {
				OptionalMedia optionalMed = (OptionalMedia) m;
				if (optionalMed.getDescription() instanceof ImageDescription) {
					ImageDescription imDesc = (ImageDescription) optionalMed.getDescription();
					File f = new File(imDesc.getLocation());
					System.out.println(imDesc.getLocation() + "  " + f.length());
				}else{
					VideoDescription vidDesc = (VideoDescription) optionalMed.getDescription();
					File f = new File("playList/Videos/"+vidDesc.getLocation());
					op_list.add(vidDesc.getLocation());
					videoInfo.put(vidDesc.getLocation(), vidDesc.getDuration());
					System.out.println(vidDesc.getLocation() + "  " + f.length());
				}

			} else {
				AlternativesMedia alternativesMed = (AlternativesMedia) m;
				for (MediaDescription mDesc : alternativesMed.getMedias()) {
					if (mDesc instanceof VideoDescription) {
						VideoDescription vidDesc = (VideoDescription) mDesc;
						File f = new File("playList/Videos/" + vidDesc.getLocation());
						alt_list.add(vidDesc.getLocation());
						videoInfo.put(vidDesc.getLocation(), vidDesc.getDuration());
						System.out.println(vidDesc.getLocation() + "  " + f.length());
					}
					else{
						ImageDescription imDesc =(ImageDescription) mDesc;
						File f = new File("playList/Videos/"+imDesc.getLocation());
						System.out.println(imDesc.getLocation()+ "  " + f.length());
					}
				}
			}

		}
		List<List<String>> combin_op_list = allCombinations(op_list);
		op_man_alt_list = listVariants(combin_op_list, alt_list, man_list);
		return op_man_alt_list;
	}

	/**
	 * fonction auxiliaire pour calcul de toutes variantes possible
	 * 
	 * @param op  liste de listes des videos Optional
	 * @param alt liste des video Alternative
	 * @param man liste de videos Mandatory
	 * @return la liste des listes de variantes possibles
	 */
	public List<List<String>> listVariants(List<List<String>> op, List<String> alt, List<String> man) {
		List<List<String>> op_man = new ArrayList<List<String>>();
		for (List<String> listOpMan : op) {
			List<String> list_op_man = new ArrayList<String>();
			list_op_man.addAll(listOpMan);
			list_op_man.addAll(man);
			op_man.add(list_op_man);
		}
		List<List<String>> op_man_alt = new ArrayList<List<String>>();
		int i_op_man = 0;
		int i_alt = 0;
		while (i_op_man < op_man.size()) {
			List<String> list_sans_alt = op_man.get(i_op_man);
			while (i_alt < alt.size()) {
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

	/**
	 * Calcule les combinaisons possibles des videos Optional
	 * 
	 * @param optList liste des videos Optional
	 * @return la liste de liste de variantes possibles
	 */

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

	/*
	 * Creation des fichiers .txt de toutes les variantes. Ces fichiers seront
	 * utilisés par FFMPEG pour generer les mp4 ======= /** Crée une ligne
	 * "'file' video" dans le fichier variante.txt
	 * 
	 * @param name nom de la video
	 * 
	 * @param index index de la variante
	 */
	private void execEcho(String name, int index) {
		String s;
		Process p;
		String variante = "variante" + index;
		if (playListInfo.get(variante) == null) {
			ArrayList<String> l = new ArrayList<String>();
			l.add(name);
			playListInfo.put(variante, l);
		} else {
			(playListInfo.get(variante)).add(name);
		}
		try {
			String[] commands = { "bash", "-c", "echo \"file " + "\'$PWD/playList/Videos/" + name + "\'"
					+ "\" >> playList/variantes/" + variante + ".txt" };
			p = Runtime.getRuntime().exec(commands);

			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((s = br.readLine()) != null)
				System.out.println("line: " + s);
			p.waitFor();
			p.destroy();
		} catch (Exception e) {
		}

	}

	/**
	 * Execute la commande FFMPEG
	 * 
	 * @param typeCommand type de la commande (image, video ou gif)
	 * @param videoName   le nom de la video
	 */

	private void execFFMPEG(String typeCommand, String videoName) {
		String cmd = "";
		switch (typeCommand) {
		case ADDVIDEO:
			cmd = "ffmpeg -safe 0 -f concat -segment_time_metadata 1 -i playList/variantes/" + videoName
					+ ".txt -vf select=concatdec_select -af aselect=concatdec_select,aresample=async=1 playList/VideosGenerated/"
					+ videoName + ".mp4";
			break;
		case ADDIMAGE:
			cmd = "ffmpeg -y -i playList/VideosGenerated/" + videoName
					+ ".mp4 -r 1 -t 00:00:03 -ss 00:00:03 -f image2 playList/Images/" + videoName + ".png";
			break;
		case ADDGIF:
			cmd = "ffmpeg -ss 3.0 -t 7.0 -i playList/VideosGenerated/" + videoName + ".mp4 -f gif playList/Gif/"
					+ videoName + ".gif";
			break;
		// Calculer la duree de la video
		case DURATION:
			cmd = "ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 " + videoName
					+ ".mp4";
			break;
		default:
			throw new IllegalArgumentException("Type of command must be : video, image or gif");
		}
		try {
			System.out.println(cmd);
			String[] a = new String[] { "/bin/sh", "-c", cmd };
			Process p = Runtime.getRuntime().exec(a);
			p.waitFor();
			p.destroy();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @param dirName path vers un repertoire
	 * @return un tableau de fichiers .txt qui se trouvent dans le repertoire
	 */
	public File[] finder(String dirName) {
		File dir = new File(dirName);
		return dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".txt");
			}
		});

	}

	/**
	 * Fonction qui fait appel à la generation des videos, images et GIF
	 */
	private void generateAllVideosImagesGifs() {
		File[] playlists = finder("playList/variantes/");
		for (File f : playlists) {
			String nameFile = f.getName().split("\\.(?=[^\\.]+$)")[0];
			execFFMPEG(ADDVIDEO, nameFile);
			execFFMPEG(ADDIMAGE, nameFile);
			execFFMPEG(ADDGIF, nameFile);
		}
	}

	/*
	 * Creation du fichier CSV, avec toutes les variantes
	 */
	public void generateCSVfile() {
		try (PrintWriter writer = new PrintWriter(new File("playList/Variantes.csv"))) {
			StringBuilder sb = new StringBuilder();
			sb.append(" ,");
			Set<String> names = videoInfo.keySet();
			Set<String> playlist = playListInfo.keySet();
			Collection<ArrayList<String>> vidNames = playListInfo.values();
			for (String s : names) {
				sb.append(s + ",");
			}
			sb.append("\n");

			for (String plist : playlist) {
				sb.append(plist + ",");
				for (String s : names) {
					if (playListInfo.get(plist).contains(s)) {
						sb.append("True,");
					} else {
						sb.append("False,");
					}
				}
				sb.append("\n");
			}
			writer.write(sb.toString());

			System.out.println("done!");

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Fonction qui fait appel à la génération des variantes (un fichier pas
	 * variante) et les videos, images et GIF
	 */

	private void generateData() {
		int i = 0;
		for (List<String> l : genVariantesFromVideoGeneratorModel()) {
			i++;

			System.out.println(l);

			for (String video : l) {
				execEcho(video, i);
			}
		}

		generateAllVideosImagesGifs();
		generateCSVfile();
	}
	/**
	 * main pour test
	 */

	public static void main(String[] args) throws InterruptedException {
		VideoGenTest1 v = new VideoGenTest1();
		v.generateData();
	}

}
