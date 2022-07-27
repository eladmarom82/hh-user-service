package puller

import java.nio.file.{Files, Paths}
import javax.inject.Inject

class PullerApp @Inject()() {

  def pull(clientId: Int, yyyyMMdd: Int): Long = {
    val inputFileUri = getClass.getResource(s"/input/client${clientId}_$yyyyMMdd.csv").toURI
    val stream = Files.lines(Paths.get(inputFileUri))
    val numLines = stream.count
    stream.close()
    numLines
  }

}
