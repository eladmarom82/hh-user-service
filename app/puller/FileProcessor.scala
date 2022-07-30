package puller

import dal.AppUserDal
import helpers.StreamUtil
import module.ProcessingConfig
import parsers.AppUserParser
import play.api.Logger

import java.net.URI
import java.nio.file.{Files, Paths}
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.jdk.StreamConverters._

/**
 * in charge of async batch parsing and storing of the file content
 */
class FileProcessor @Inject()(appUserDal: AppUserDal, config: ProcessingConfig) {

  private val logger = Logger(getClass)

  def process(fileURI: URI, yyyyMMdd: Int, parser: AppUserParser): Future[LinesSummary] = {
    logger.info(s"start processing $fileURI using batch size of ${config.batchSize}")

    val sourceStream = Files.lines(Paths.get(fileURI))
    val parallelBatchStream = StreamUtil.batch(sourceStream.iterator, config.batchSize).parallel

    val futures: List[Future[LinesSummary]] = parallelBatchStream.map { list =>
      var failedLines = 0
      val records = list.flatMap { line =>
        parser.parse(line).fold(
          throwable => {
            logger.error(s"error while parsing the following line: $line", throwable)
            failedLines += 1
            // increment metric / raise alert ...
            Option.empty
          }, Some(_)
        )
      }

      appUserDal.insertBatchAsync(records, yyyyMMdd).map(_ => LinesSummary(records.size, failedLines))
    }.toScala(List)

    Future.sequence(futures)
      .map { list =>
        parallelBatchStream.close()
        sourceStream.close()
        list.reduce(_ + _)
      }
  }
}
