package services

import parsers.AppUserRecord
import play.api.db.Database

import javax.inject.Inject

class AppUserService @Inject()(db: Database) {

  def update(record: AppUserRecord, lastUpdate: Long): Boolean = db.withConnection(_.createStatement.execute(
    s"""INSERT INTO AppUser (ClientId, EmployeeId, FirstName, LastName, BirthDate, LastUpdated)
       |VALUES (${record.clientId}, ${record.employeeId}, '${record.firstName}', '${record.lastName}', '${record.birthDate}', $lastUpdate)
       |ON DUPLICATE KEY UPDATE FirstName   = '${record.firstName}',
       |                        LastName    = '${record.lastName}',
       |                        BirthDate   = '${record.birthDate}',
       |                        LastUpdated = $lastUpdate;
       |""".stripMargin
  ))

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
