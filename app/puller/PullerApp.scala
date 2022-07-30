package puller

import dal.{AppUserDal, ClientDal}
import parsers.AppUserParser

import java.time.Duration
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * this object is in charge of the async pulling and storing process
 */
class PullerApp @Inject()(fileProvider: FileProvider, fileProcessor: FileProcessor,
                          appUserDal: AppUserDal, clientDal: ClientDal) {

  def pull(clientId: Int, yyyyMMdd: Int): Future[(LinesSummary, Duration)] = {
    val startTime = System.currentTimeMillis()

    for {
      parser <- Future.fromTry(AppUserParser.create(clientId))
      inputFileUri <- fileProvider.getFile(clientId, yyyyMMdd)
      _ <- appUserDal.clean(yyyyMMdd)
      linesSummary <- fileProcessor.process(inputFileUri, yyyyMMdd, parser)
      _ <- clientDal.update(clientId, yyyyMMdd)
      _ <- appUserDal.cleanAllBut(yyyyMMdd)
    } yield (linesSummary, Duration.ofMillis(System.currentTimeMillis() - startTime))
  }
}
