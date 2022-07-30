package puller

import java.io.FileNotFoundException
import java.net.URI
import scala.concurrent.Future
import scala.util.{Failure, Try}

/**
 * the object in charge of fetching the file locally from any file storage, in our case from conf dir
 */
class FileProvider {

  def getFile(clientId: Int, yyyyMMdd: Int): Future[URI] = {
    val pathInConfDir = s"/input/client${clientId}_$yyyyMMdd.csv"

    Future.fromTry(
      Try(getClass.getResource(pathInConfDir)).map(_.toURI)
        .orElse(Failure(new FileNotFoundException(s"file not found: conf$pathInConfDir")))
    )
  }

}
