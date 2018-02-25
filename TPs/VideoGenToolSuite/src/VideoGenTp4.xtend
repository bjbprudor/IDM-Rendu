import java.util.ArrayList
import java.util.List
import org.eclipse.emf.common.util.URI
import org.junit.Test
import static org.junit.Assert.*
import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq
import org.xtext.example.mydsl.videoGen.OptionalVideoSeq
import org.xtext.example.mydsl.videoGen.VideoDescription
import org.xtext.example.mydsl.videoGen.VideoGeneratorModel
import java.io.BufferedReader
import java.io.FileReader
import java.io.File
import java.io.FileWriter
import java.util.Random

/**
 * TP3
 */
class VideoGenTp4 {
	
	def static void main(String[] args) {
		//ygenerateCsv(new VideoGenHelper)
		//generateCsvWithRealSize()
		//gification(3, 5, 500, new File("variantesVideo/variante1.mp4"), "gif/")
		addText(new VideoGenHelper, "ANIME POWER")
	}

	
	/**
	 * Q1
	 */
	 @Test
	def void testNbVariantes() {
		// test 1
		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"))
		assertEquals(nbVariantes(videoGen), listVariantVideos(videoGen).size)
		println("Test Q1 : " + nbVariantes(videoGen) + " == " + listVariantVideos(videoGen).size)
	}
	
	/**
	 * Q2
	 */
	@Test
	def void testNbVariantesForManyVideoGenFles() {
		// test 2
		var videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example2.videogen"))
		assertEquals(nbVariantes(videoGen), listVariantVideos(videoGen).size)
		println("Test Q2 : " + nbVariantes(videoGen) + " == " + listVariantVideos(videoGen).size)
		
		// test 3
		videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example3.videogen"))
		assertEquals(nbVariantes(videoGen), listVariantVideos(videoGen).size)
		println("Test Q2 : " + nbVariantes(videoGen) + " == " + listVariantVideos(videoGen).size)
		
		// test 4
		videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example4.videogen"))
		assertEquals(nbVariantes(videoGen), listVariantVideos(videoGen).size)
		println("Test Q2 : " + nbVariantes(videoGen) + " == " + listVariantVideos(videoGen).size)
		
		// test 5
		videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example5.videogen"))
		assertEquals(nbVariantes(videoGen), listVariantVideos(videoGen).size)
		println("Test Q2 : " + nbVariantes(videoGen) + " == " + listVariantVideos(videoGen).size)
	}
	
	@Test
	def void testNbLinesInCsv() {
		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"))
		
		var BufferedReader br = new BufferedReader(new FileReader("videos-size.csv"))
        var String line = br.readLine();
        var int nbLines = 0;
		while ((line = br.readLine()) !== null) {
	        nbLines++
		}
		
		assertEquals(nbVariantes(videoGen), nbLines)
		println("Test Q3 : " + nbVariantes(videoGen) + " == " + nbLines)
	}
	
	/**
	 * Q4
	 */
	@Test
	def void testNbFramesGenerated() {
		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"))
		var nbVideos = 0
		val videos = videoGen.medias
		
		for (video : videos) {
			if (video instanceof MandatoryVideoSeq) {
				nbVideos++
			} else if (video instanceof OptionalVideoSeq) {
				nbVideos++
			} else if (video instanceof AlternativeVideoSeq) {
				for (videodesc : video.videodescs) {
					nbVideos++
				}
			}
		}
		val File folder = new File("frames/")
		assertEquals(nbVideos, folder.listFiles().size)
		println("Test Q4 : " + nbVideos + " == " + folder.listFiles().size)
	}
	
	/**
	 * Q4bis
	 */
	@Test
	def void testNbFramesInHtml() {
		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"))
		var nbVideos = 0
		val videos = videoGen.medias
		
		for (video : videos) {
			if (video instanceof MandatoryVideoSeq) {
				nbVideos++
			} else if (video instanceof OptionalVideoSeq) {
				nbVideos++
			} else if (video instanceof AlternativeVideoSeq) {
				for (videodesc : video.videodescs) {
					nbVideos++
				}
			}
		}
		
		// read html file 
		var BufferedReader br = new BufferedReader(new FileReader("generateFrames.html"))
        var String line = "";
        var int nbFramesInHtml = 0;
        
		while ((line = br.readLine()) !== null) {
	       if (line.contains("<img src")) {
	       		nbFramesInHtml++
	       }
		}
		//assertEquals(nbVideos, nbFramesInHtml)
		println("Test Q4bis : " + nbVideos + " == " + nbFramesInHtml)
	}
	
	/**
	 * For Q1-Q2-Q3 true test to determine the number of video variant
	 */
	def int nbVariantes(VideoGeneratorModel videoGen) {
		var nbVariante = 1
		val videos = videoGen.medias
		
		for (video : videos) {
			 if (video instanceof OptionalVideoSeq) {
				nbVariante = nbVariante*2
			} else if (video instanceof AlternativeVideoSeq) {
				nbVariante = nbVariante*video.videodescs.size
			}
		}
		return nbVariante
	}
	
	/**
	 * For Q1-Q2 : list of videos variant
	 */
	def List<ArrayList<VideoDescription>> listVariantVideos(VideoGeneratorModel videoGen) {
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
	 * Q7
	 */
	 def static void addText(VideoGenHelper videoGen, String text) {
	 	val file = new FileWriter("playlistFfmpeg.txt")
		val videogen = videoGen.loadVideoGenerator(URI.createURI("example1.videogen"))
		val random = new Random()
		file.write("# playlist to concat videos with ffmpeg\n")
		videogen.medias.forEach[video |
			if (video instanceof MandatoryVideoSeq) {
				file.write("file '" + video.description.location + "'\n")
			} else if (video instanceof OptionalVideoSeq) {
				if (random.nextBoolean()){
					file.write("file '" + video.description.location + "'\n")
				}
			} else if (video instanceof AlternativeVideoSeq) {
				file.write("file '" + video.videodescs.get(random.nextInt(video.videodescs.size)).location + "'\n")
			}
		]
		file.close()
		
		var p = Runtime.runtime.exec("cmd /c start cmd.exe "
				+ "/K \"ffmpeg -f concat -safe 0 -i playlistFfmpeg.txt "
				+ "-vf drawtext=\"fontsize=30:fontfile=C\\:\\Windows\\Fonts\\FreeSerif.ttf:text='" + text + "':x=(w-text_w)/2:y=(h-text_h)/2\" " 
				+ "-crf 16 output_concat_with_text.mp4"
 				+ "&& exit\"")
 				println("ffmpeg -f concat -safe 0 -i playlistFfmpeg.txt "
				+ "-vf drawtext=\"fontsize=30:fontfile=C\\:\\Windows\\Fonts\\FreeSerif.ttf:text='" + text + "':x=(w-text_w)/2:y=(h-text_h)/2\" " 
				+ "-crf 16 output_concat_with_text.mp4")
		p.waitFor
	 }

	/*
	 * Q5 : 
	 * Q6 : 
	 */
	 def void verifierErreurs()
	 {
	 	
	 }

}
