package dal

import play.api.db.Database

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ClientDal @Inject()(db: Database) {

  def update(clientId: Int, lastUpdate: Long) = Future {
    db.withConnection(_.createStatement.execute(
      s"""INSERT INTO ClientLastUpdate (ClientId, LastUpdated)
         |VALUES ($clientId, $lastUpdate)
         |ON DUPLICATE KEY UPDATE LastUpdated = $lastUpdate;
         |""".stripMargin
    ))
  }

}
