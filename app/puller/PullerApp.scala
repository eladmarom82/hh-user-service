package puller

import dal.{AppUserDal, ClientDal}
import helpers.StreamUtil
import parsers.AppUserParser

import java.nio.file.{Files, Paths}
import java.time.Duration
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.jdk.StreamConverters._

class PullerApp @Inject()(appUserDal: AppUserDal, clientDal: ClientDal) {

  val batchSize = 2048

  def pull(clientId: Int, yyyyMMdd: Int): Future[Duration] = {
    val inputFileUri = getClass.getResource(s"/input/client${clientId}_$yyyyMMdd.csv").toURI
    val parser = AppUserParser.create(1).apply()
    val startTime = System.currentTimeMillis()
    val stream = Files.lines(Paths.get(inputFileUri))

    appUserDal.clean(yyyyMMdd)

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
    }.map { _ =>
      clientDal.update(clientId, yyyyMMdd)
      appUserDal.cleanAllBut(yyyyMMdd)
      Duration.ofMillis(System.currentTimeMillis() - startTime)
    }
  }

}
