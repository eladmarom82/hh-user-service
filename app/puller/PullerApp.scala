package puller

import dal.{AppUserDal, ClientDal}
import helpers.StreamUtil
import parsers.AppUserParser

import java.io.FileNotFoundException
import java.net.URI
import java.nio.file.{Files, Paths}
import java.time.Duration
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.jdk.StreamConverters._

class PullerApp @Inject()(appUserDal: AppUserDal, clientDal: ClientDal) {

  val batchSize = 2048

  def pull(clientId: Int, yyyyMMdd: Int): Future[Duration] = {
    val pathInConfDir = s"/input/client${clientId}_$yyyyMMdd.csv"

    Option(getClass.getResource(pathInConfDir)).map(_.toURI).fold(
      Future.failed[Duration](new FileNotFoundException(s"file not found: conf$pathInConfDir"))
    ) { inputFileUri =>
      val startTime = System.currentTimeMillis()

      for {
        _ <- appUserDal.clean(yyyyMMdd)
        _ <- processFile(inputFileUri, clientId, yyyyMMdd)
        _ <- clientDal.update(clientId, yyyyMMdd)
        _ <- appUserDal.cleanAllBut(yyyyMMdd)
      } yield Duration.ofMillis(System.currentTimeMillis() - startTime)
    }
  }

  private def processFile(fileURI: URI, clientId: Int, yyyyMMdd: Int) = {
    val stream = Files.lines(Paths.get(fileURI))
    val parser = AppUserParser.create(clientId).apply()

    Future.sequence {
      StreamUtil.batch(stream.iterator, batchSize).parallel.map { list =>
        val records = list.flatMap { line =>
          parser.parse(line).fold(
            e => {
              // log.error / increment metric / raise alert ...
              Option.empty
            }, Some(_)
          )
        }

        appUserDal.insertBatchAsync(records, yyyyMMdd)
      }.toScala(List)
    }
  }
}
