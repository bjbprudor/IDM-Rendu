import org.eclipse.emf.common.util.URI
import org.xtext.example.mydsl.videoGen.MandatoryVideoSeq
import org.xtext.example.mydsl.videoGen.OptionalVideoSeq
import org.xtext.example.mydsl.videoGen.AlternativeVideoSeq
import java.util.Random
import java.io.FileWriter
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * TP2
 */
class VideoGenTp2 {
	
	static String player = "ffplay.exe -autoexit -an ";
	
	def static void main(String[] args) {
		//modelToModelWithMPC()
		modelToTextWithFfmpeg(new VideoGenHelper)
		//durationMaxOfOneSequence(new VideoGenHelper)
		//generateFramesForEachVideo(new VideoGenHelper, "frames/")
		//generateHtmlWithFrames(new VideoGenHelper, "frames/")
	}
	
	/**
	 * Q1
	 */
	def static void modelToModelWithMPC() {
		val random = new Random()
		val videoGen = new VideoGenHelper().loadVideoGenerator(URI.createURI("example1.videogen"))
		println(videoGen.information.authorName)
		// and then visit the model
		// eg access video sequences: videoGen.videoseqs
		videoGen.medias.forEach [ video |
			if (video instanceof MandatoryVideoSeq) {
				val desc = video.description
				println("Playing..." + desc.videoid)
				var p = Runtime.runtime.exec(player + desc.location + "")
				p.waitFor
			} else if (video instanceof OptionalVideoSeq) {
				if (random.nextBoolean) {
					val desc = video.description
					println("Playing..." + desc.videoid)
					var p = Runtime.runtime.exec(player + desc.location + "")
					p.waitFor
				}
			} else if (video instanceof AlternativeVideoSeq) {
				val videoSelected = video.videodescs.get(random.nextInt(video.videodescs.size));
				println("Playing..." + videoSelected.videoid)
				var p = Runtime.runtime.exec(player + videoSelected.location + "")
				p.waitFor
			}
		]
	}
	
	/**
	 * Q2
	 */
	def static void modelToTextWithFfmpeg(VideoGenHelper videoGen) {
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
				+ "/K \" ffmpeg -f concat -safe 0 -i playlistFfmpeg.txt -c copy output_concat.mp4 "
 				+ "&& exit\"")
		p.waitFor
		
	}
	
	/**
	 * Q3
	 */
	def static int durationMaxOfOneSequence(VideoGenHelper videoGen) {
		var durationMax = 0;
		var durationMaxOfAlternativeVideo = 0;
		var videogen = videoGen.loadVideoGenerator(URI.createURI("example1.videogen"))
		val videos = videogen.medias
		for (video : videos) {
			if (video instanceof MandatoryVideoSeq) {
				//file.write("file '" + video.description.location + "'\n")
				var duration = durationOfVideo(video.description.location)
				video.description.duration = duration
				durationMax = durationMax + duration
			} else if (video instanceof OptionalVideoSeq) {
				val duration = durationOfVideo(video.description.location)
				video.description.duration = duration
				durationMax = durationMax + duration
			} else if (video instanceof AlternativeVideoSeq) {
				for (videodesc : video.videodescs){
					val duration = durationOfVideo(videodesc.location)
					videodesc.duration = duration
					if (durationMaxOfAlternativeVideo < duration) {
						durationMaxOfAlternativeVideo = duration
					}
				}
				durationMax = durationMax + durationMaxOfAlternativeVideo
			}
		}
		println("durationMax : " + durationMax + " secondes")		
		return durationMax;
	}
	
	/**
	 * For Q3
	 */
	def static int durationOfVideo(String videoPath) {
		val process = Runtime.getRuntime().exec(
			"ffprobe -v error -select_streams v:0 -show_entries stream=duration -of default=noprint_wrappers=1:nokey=1 -i " + videoPath)
		process.waitFor()
		var BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		val String outputCmd = reader.readLine();
	    val duration = Math.round(Float.parseFloat(outputCmd))
	    println("duration of " + videoPath + " : " + duration + " secondes")
	    return duration
	}
	
	/**
	 * Q4
	 */
	def static void generateFramesForEachVideo(VideoGenHelper videoGen, String outputFolder) {
		var videogen = videoGen.loadVideoGenerator(URI.createURI("example1.videogen"))
		val videos = videogen.medias
		for (video : videos) {
			if (video instanceof MandatoryVideoSeq) {
				val process = Runtime.getRuntime().exec(
					"ffmpeg -y -i " + video.description.location + " -r 1 -t 00:00:01 -ss 00:00:02 " 
					+ outputFolder + video.description.videoid + "-mandatory.jpg")
				process.waitFor()
				println("frame generated : " + video.description.videoid + "-mandatory.jpg")
			} else if (video instanceof OptionalVideoSeq) {
				val process = Runtime.getRuntime().exec(
					"ffmpeg -y -i " + video.description.location + " -r 1 -t 00:00:01 -ss 00:00:02 " 
					+ outputFolder + video.description.videoid + "-optional.jpg")
				process.waitFor()
				println("frame generated : " + video.description.videoid + "-optional.jpg")
			} else if (video instanceof AlternativeVideoSeq) {
				for (videodesc : video.videodescs){
					val process = Runtime.getRuntime().exec(
						"ffmpeg -y -i " + videodesc.location + " -r 1 -t 00:00:01 -ss 00:00:02 " 
						+ outputFolder + videodesc.videoid + "-alternative.jpg")
					process.waitFor()
					println("frame generated : " + videodesc.videoid + "-alternative.jpg")
				}
			}
		}
	}
	
	/**
	 * Q5
	 */
	def static void generateHtmlWithFrames(VideoGenHelper videoGen, String outputFolder) {
		val file = new FileWriter("generateFrames.html")
		var String framesMandatory = "";
		var String framesOptional = ""; 
		var String framesAlternative = "";
		var videogen = videoGen.loadVideoGenerator(URI.createURI("example1.videogen"))
		val videos = videogen.medias
		
		for (video : videos) {
			if (video instanceof MandatoryVideoSeq) {
				val process = Runtime.getRuntime().exec(
					"ffmpeg -y -i " + video.description.location + " -r 1 -t 00:00:01 -ss 00:00:02 " 
					+ outputFolder + video.description.videoid + "-mandatory.jpg")
				process.waitFor()
				println("frame generated : " + video.description.videoid + "-mandatory.jpg")
				framesMandatory += "<p><b>Mandatory</b></p>\n
					<img src = " + outputFolder + video.description.videoid + "-mandatory.jpg" + " width='300px' height=auto/><br/>\n"
			} else if (video instanceof OptionalVideoSeq) {
				val process = Runtime.getRuntime().exec(
					"ffmpeg -y -i " + video.description.location + " -r 1 -t 00:00:01 -ss 00:00:02 " 
					+ outputFolder + video.description.videoid + "-optional.jpg")
				process.waitFor()
				println("frame generated : " + video.description.videoid + "-optional.jpg")
				framesOptional += "<p><b>Optional</b></p>\n
					<img src = " + outputFolder + video.description.videoid + "-optional.jpg" + " width='300px' height=auto/><br/>\n"
			} else if (video instanceof AlternativeVideoSeq) {
				framesAlternative += "<p><b>Alternatives</b></p>\n"
				for (videodesc : video.videodescs){
					val process = Runtime.getRuntime().exec(
						"ffmpeg -y -i " + videodesc.location + " -r 1 -t 00:00:01 -ss 00:00:02 " 
						+ outputFolder + videodesc.videoid + "-alternative.jpg")
					process.waitFor()
					println("frame generated : " + videodesc.videoid + "-alternative.jpg")
					framesAlternative += "<img src = " + outputFolder + videodesc.videoid + "-alternative.jpg" + " width='300' height=auto/><br/>\n"
				}
			}
		}
		file.write(
			"<!DOCTYPE html>\n"
			+ "<head>\n"
			+ "</head>\n"
			+ "<body>\n"
			+ framesMandatory
			+ framesOptional
			+ framesAlternative
			+ "</body>"
			+ "</html>"
		)
		file.close()
	}
}
