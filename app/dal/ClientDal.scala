package dal

import play.api.db.Database

import javax.inject.Inject

class ClientDal @Inject()(db: Database) {

  def update(clientId: Int, lastUpdate: Long): Boolean = db.withConnection(_.createStatement.execute(
    s"""INSERT INTO ClientLastUpdate (ClientId, LastUpdated)
       |VALUES ($clientId, $lastUpdate)
       |ON DUPLICATE KEY UPDATE LastUpdated = $lastUpdate;
       |""".stripMargin
  ))

}
