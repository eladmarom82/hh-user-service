package dal

import parsers.AppUserRecord
import play.api.db.Database

import javax.inject.Inject
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AppUserDal @Inject()(db: Database) {

  def insertBatchAsync(records: ListBuffer[AppUserRecord], lastUpdate: Long) = Future {
    if (records.nonEmpty) {
      db.withConnection(connection => connection.createStatement.execute(
        s"""INSERT INTO AppUser (ClientId, EmployeeId, FirstName, LastName, BirthDate, LastUpdated)
           |VALUES ${records.map(record => s"(${record.clientId}, ${record.employeeId}, '${record.firstName}', '${record.lastName}', '${record.birthDate}', $lastUpdate)").mkString(",")};
           |""".stripMargin
      ))
    } else false
  }

  def clean(currentSnapshot: Long) = db.withConnection(_.createStatement.execute(
    s"DELETE FROM AppUser WHERE LastUpdated = $currentSnapshot"
  ))

  def cleanAllBut(currentSnapshot: Long) = db.withConnection(_.createStatement.execute(
    s"DELETE FROM AppUser WHERE LastUpdated <> $currentSnapshot"
  ))

  def isUserEligibleForRegistration(clientId: Int, employeeId: Int) = Future {
    db.withConnection { connection =>
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


}
