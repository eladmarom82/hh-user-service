package controllers

import db.DbService
import play.api.mvc.{AnyContent, InjectedController, Request}

import javax.inject.Inject

class InitController @Inject()(dbService: DbService) extends InjectedController {

  def init() = Action { implicit request: Request[AnyContent] =>
    dbService.runCommand(
      """CREATE TABLE IF NOT EXISTS User
        |(
        |  ClientId             INT,
        |  EmployeeId           INT,
        |  FirstName       VARCHAR(50),
        |  LastName        VARCHAR(50)
        |);
        |""".stripMargin
    )

    dbService.runCommand(
      """CREATE TABLE IF NOT EXISTS VideoValidation
        |(
        |  ClientId             INT,
        |  SnapshotTimestamp    INT
        |);
        |""".stripMargin
    )

    Ok(s"initalized")
  }

}
