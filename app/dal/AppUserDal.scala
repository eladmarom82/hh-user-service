package dal

import parsers.AppUserRecord
import play.api.db.Database

import javax.inject.Inject
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AppUserDal @Inject()(db: Database) {

  def update(record: AppUserRecord, lastUpdate: Long) = db.withConnection(connection => connection.createStatement.execute(
    s"""INSERT INTO AppUser (ClientId, EmployeeId, FirstName, LastName, BirthDate, LastUpdated)
       |VALUES (${record.clientId}, ${record.employeeId}, '${record.firstName}', '${record.lastName}', '${record.birthDate}', $lastUpdate)
       |ON DUPLICATE KEY UPDATE FirstName   = '${record.firstName}',
       |                        LastName    = '${record.lastName}',
       |                        BirthDate   = '${record.birthDate}',
       |                        LastUpdated = $lastUpdate;
       |""".stripMargin
  ))

  def insertBatchAsync(records: ListBuffer[AppUserRecord], lastUpdate: Long) = Future {
    db.withConnection(connection => connection.createStatement.execute(
    s"""INSERT INTO AppUser (ClientId, EmployeeId, FirstName, LastName, BirthDate, LastUpdated)
       |VALUES ${records.map(record => s"(${record.clientId}, ${record.employeeId}, '${record.firstName}', '${record.lastName}', '${record.birthDate}', $lastUpdate)").mkString(",")};
       |""".stripMargin
    ))
  }

  def isUserEligibleForRegistration(clientId: Int, employeeId: Int): Boolean = db.withConnection { connection =>
    val resultSet = connection.createStatement.executeQuery(
      s"""SELECT * FROM AppUser au
         | JOIN ClientLastUpdate clu ON au.ClientId=clu.ClientId
         | WHERE au.ClientId = $clientId
         | AND  au.EmployeeId = $employeeId
         | AND  au.LastUpdated=clu.LastUpdated
         |""".stripMargin
    )

    resultSet.first()
  }

}
