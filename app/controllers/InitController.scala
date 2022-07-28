package controllers

import play.api.db.Database
import play.api.mvc.{AnyContent, InjectedController, Request}

import javax.inject.Inject

class InitController @Inject()(db: Database) extends InjectedController {

  def init() = Action { implicit request: Request[AnyContent] =>
    createAppTables

    Ok(s"initalized")
  }

  private def createAppTables = {
    db.withConnection(_.createStatement.execute("DROP TABLE IF EXISTS AppUser;"))
    db.withConnection(_.createStatement.execute("DROP TABLE IF EXISTS ClientLastUpdate;"))

    db.withConnection(_.createStatement.execute(
      """CREATE TABLE IF NOT EXISTS AppUser
        |(
        |  ClientId             INT NOT NULL,
        |  EmployeeId           INT NOT NULL,
        |  FirstName            VARCHAR(50) NOT NULL,
        |  LastName             VARCHAR(50) NOT NULL,
        |  BirthDate            DATE NOT NULL,
        |  LastUpdated          BIGINT NOT NULL,
        |  PRIMARY KEY (ClientId, EmployeeId, LastUpdated)
        |);
        |""".stripMargin))

    db.withConnection(_.createStatement.execute(
      """CREATE TABLE IF NOT EXISTS ClientLastUpdate
        |(
        |  ClientId             INT NOT NULL,
        |  LastUpdated          BIGINT NOT NULL,
        |  PRIMARY KEY (ClientId)
        |);
        |""".stripMargin))
  }
}
