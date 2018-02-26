import org.eclipse.emf.common.util.URI
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq
import org.xtext.example.mydsl.videoGen.OptionalVideoSeq
import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq
import java.io.FileWriter
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.ArrayList
import org.xtext.example.mydsl.videoGen.VideoDescription
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel
import java.util.LinkedList
import java.util.List

/**
 * TP3
 */
class VideoGenTp3 {
	
	def static void main(String[] args) {
		generateCsv(new VideoGenHelper)
		//generateCsvWithRealSize()
		//gification(3, 5, 500, new File("variantesVideo/variante1.mp4"), "gif/")
		//gifSize(3, 5, 500, new File("variantesVideo/variante1.mp4"), "gif/")
	}
	
	/**
	 * Q1
	 */
	def static void generateCsv(VideoGenHelper videoGen) {
		var String content = "";
		var videogen = videoGen.loadVideoGenerator(URI.createURI("example1.videogen"))
		var listVariantVid = new LinkedList<LinkedList<VideoDescription>>
		
		// add size for each video object of listVariant
		for (listVariantVideos : listVariantVideos(videogen)) {
			
			var temp = new LinkedList<VideoDescription>
			for (var int i = 0; i < listVariantVideos.size(); i++) {
				var VideoDescription currentVideo = listVariantVideos.get(i)
				temp.add(currentVideo)
			}
			listVariantVid.add(temp)
		}
		content += "id;"
		val videos = videogen.medias
		var List<String> nameColumns = new LinkedList<String>()
		
		for (video : videos) {
			if (video instanceof MandatoryVideoSeq) {
				nameColumns.add(video.description.videoid)
				content += video.description.videoid + ";"
			} else if (video instanceof OptionalVideoSeq) {
				nameColumns.add(video.description.videoid)
				content += video.description.videoid + ";"
			} else if (video instanceof AlternativeVideoSeq) {
				for (videodesc : video.videodescs){
					nameColumns.add(videodesc.videoid)
					content += videodesc.videoid + ";"
				}
			}
		}
		
		content += "size\n"
		
		// complete true/false and size in csv
		
		var int ii = 1 
		for (listVariantVideos : listVariantVid) {
			var int size = 0;
			content += (ii) + ";"
			var List<String> listName = new ArrayList<String>()
			for (var int i = 0; i < listVariantVideos.size(); i++) {
				val File currentFile = new File(listVariantVideos.get(i).location)
				val currentFileSize = currentFile.length() as int
				size += currentFileSize
				listName.add(listVariantVideos.get(i).videoid)
			}
			
			for (name : nameColumns) {
				if (listName.contains(name)) {
					content += "TRUE;"
				} else {
					content += "FALSE;"
				}
			}
			content += size + "\n"
			ii++
		}
		val file = new FileWriter("videos-size.csv")
		file.write(content)
		file.close()	
		println("content cvs : " + content)
	}
	
	/**
	 * For Q1
	 */
	def static listVariantVideos(VideoGeneratorModel videoGen) {
		var listVariantVideos = new ArrayList<ArrayList<VideoDescription>>
		listVariantVideos.add(new ArrayList<VideoDescription>())
		
		for (video : videoGen.medias) {
			if (video instanceof MandatoryVideoSeq) {
				
				for (list : listVariantVideos) {
					list.add(video.description)
				}
			} else if (video instanceof OptionalVideoSeq) {
				var listTmp = new ArrayList<ArrayList<VideoDescription>>
				for (list : listVariantVideos) {
					var tmp = list.clone as ArrayList<VideoDescription>
					listTmp.add(list)
					tmp.add(video.description)
					listTmp.add(tmp)
				}
				listVariantVideos = listTmp
			} else if (video instanceof AlternativeVideoSeq) {
				var listTmp = new ArrayList<ArrayList<VideoDescription>>
				for (vids : video.videodescs) {
					for (list : listVariantVideos) {
						var tmp = list.clone as ArrayList<VideoDescription>
						tmp.add(vids)
						listTmp.add(tmp)
					}
				}
				listVariantVideos = listTmp
			}
		}
		return listVariantVideos
	}
	
	/**
	 * Q2 : d'après R la valeur de corrélation est de 1 donc forte corrélation 
	 * et donc quand on concatène les vidéos ça revient au même poids fichier que les vidéos séparées
	 */
	def static void generateCsvWithRealSize() {
		var String content = "";
		
		var p = Runtime.runtime.exec("cmd /c start cmd.exe "
				+ "/K \" ffmpeg -f concat -safe 0 -i playlist1.txt -c copy variantesVideo/variante1.mp4 "
 				+ "&& exit\"")
		p.waitFor
		
		var p2 = Runtime.runtime.exec("cmd /c start cmd.exe "
				+ "/K \" ffmpeg -f concat -safe 0 -i playlist2.txt -c copy variantesVideo/variante2.mp4 "
 				+ "&& exit\"")
		p2.waitFor
		
		var p3 = Runtime.runtime.exec("cmd /c start cmd.exe "
				+ "/K \" ffmpeg -f concat -safe 0 -i playlist3.txt -c copy variantesVideo/variante3.mp4 "
 				+ "&& exit\"")
		p3.waitFor
		
		var p4 = Runtime.runtime.exec("cmd /c start cmd.exe "
				+ "/K \" ffmpeg -f concat -safe 0 -i playlist4.txt -c copy variantesVideo/variante4.mp4 "
 				+ "&& exit\"")
		p4.waitFor
	
		var BufferedReader br = new BufferedReader(new FileReader("videos-size.csv"))
        var String line = br.readLine();
        content += line
        content += ";realSize\n"
        var int i = 1;
		while ((line = br.readLine()) !== null) {
	        val File videoVariante = new File("variantesVideo/variante" + i + ".mp4")
	        i++
			content += line
			content += ";" + videoVariante.length() + "\n"
		}
		
		val file = new FileWriter("videos-realSize.csv")
		file.write(content)
		file.close()
		println(content)
	}
	
	/**
	 * Q3
	 */
	def static void gification(int start, int duration, int size, File video, String outputFolder) {
		val process1 = Runtime.getRuntime().exec("cmd /c start cmd.exe "
			+ "/K \"ffmpeg -y -ss " + start + " -t " + duration + " -i variantesVideo/" + video.getName() + " -vf fps=10,scale=" 
			+ size + ":-1:flags=lanczos,palettegen " + outputFolder + video.getName() + "-palette.png"
			+ "&& ffmpeg -ss " + start + " -t " + duration + " -i variantesVideo/" + video.getName()
			+ " -i gif/" + video.getName() + "-palette.png -filter_complex \"fps=10,scale="
		 	+ size + ":-1:flags=lanczos[x];[x][1:v]paletteuse\" " + outputFolder + video.getName() + ".gif"
			+ "&& exit\"")
		process1.waitFor()
		println("palette generated : " + outputFolder + video.getName() + "-palette.png")
		println("gif generated : " + outputFolder + video.getName() + ".gif")
	}
	
	/**
	 * For Q4 : logiquement la taille du gif dépend de la durée définie, donc pour la comparaison des tailles ce sera presque les mêmes 
	 * valeurs comme pour la comparaison des tailles des vidéos
	 */
	def static long gifSize(int start, int duration, int size, File video, String outputFolder) {
		// même code que q3
		val process1 = Runtime.getRuntime().exec("cmd /c start cmd.exe "
			+ "/K \"ffmpeg -y -ss " + start + " -t " + duration + " -i variantesVideo/" + video.getName() + " -vf fps=10,scale=" 
			+ size + ":-1:flags=lanczos,palettegen " + outputFolder + video.getName() + "-palette.png"
			+ "&& ffmpeg -ss " + start + " -t " + duration + " -i variantesVideo/" + video.getName()
			+ " -i gif/" + video.getName() + "-palette.png -filter_complex \"fps=10,scale="
		 	+ size + ":-1:flags=lanczos[x];[x][1:v]paletteuse\" " + outputFolder + video.getName() + ".gif"
			+ "&& exit\"")
		process1.waitFor()
		println("palette generated : " + outputFolder + video.getName() + "-palette.png")
		println("gif generated : " + outputFolder + video.getName() + ".gif")
		
		val File sizeFileGif = new File(outputFolder + video.getName() + ".gif") 
		println("size of gif : " + sizeFileGif.length() + " bytes")
		return sizeFileGif.length()
	}
}
