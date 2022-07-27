package controllers

import javax.inject._
import play.api.mvc._

/**
 * This controller represents the endpoint to the service to which we interact when determining users app eligibility
 */
@Singleton
class AppUserController extends InjectedController {

  def check(clientId: Int, employeeId: Int) = Action { implicit request: Request[AnyContent] =>
    Ok(s"clientId: $clientId, employeeId: $employeeId")
  }
}
