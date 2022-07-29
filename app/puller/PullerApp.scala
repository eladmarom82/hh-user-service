package puller

import dal.{AppUserDal, ClientDal}

import java.io.FileNotFoundException
import java.time.Duration
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PullerApp @Inject()(fileProcessor: FileProcessor, appUserDal: AppUserDal, clientDal: ClientDal) {

  def pull(clientId: Int, yyyyMMdd: Int): Future[Duration] = {
    val pathInConfDir = s"/input/client${clientId}_$yyyyMMdd.csv"

    Option(getClass.getResource(pathInConfDir)).map(_.toURI).fold(
      Future.failed[Duration](new FileNotFoundException(s"file not found: conf$pathInConfDir"))
    ) { inputFileUri =>
      val startTime = System.currentTimeMillis()

      for {
        _ <- appUserDal.clean(yyyyMMdd)
        _ <- fileProcessor.process(inputFileUri, clientId, yyyyMMdd)
        _ <- clientDal.update(clientId, yyyyMMdd)
        _ <- appUserDal.cleanAllBut(yyyyMMdd)
      } yield Duration.ofMillis(System.currentTimeMillis() - startTime)
    }
  }
}
