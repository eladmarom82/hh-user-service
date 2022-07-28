package puller

import dal.{AppUserDal, ClientDal}
import parsers.AppUserParser

import java.nio.file.{Files, Paths}
import javax.inject.Inject

class PullerApp @Inject()(appUserDal: AppUserDal, clientDal: ClientDal) {

  def pull(clientId: Int, yyyyMMdd: Int): Long = {
    val inputFileUri = getClass.getResource(s"/input/client${clientId}_$yyyyMMdd.csv").toURI
    val stream = Files.lines(Paths.get(inputFileUri)).parallel()
    val parser = AppUserParser.create(1).apply()

    stream.forEach { line =>
      parser.parse(line).fold(
        e => {
          // log.error / increment metric / raise alert ...
        },
        record => appUserDal.update(record, yyyyMMdd)
      )
    }

    clientDal.update(clientId, yyyyMMdd)
    stream.count
  }

}
