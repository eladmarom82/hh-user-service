package controllers

import dal.AppUserDal
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * This controller represents the endpoint to the service to which we interact when determining users app eligibility
 */
@Singleton
class AppUserController @Inject()(appUserDal: AppUserDal) extends InjectedController {

  def check(clientId: Int, employeeId: Int) = Action.async { implicit request: Request[AnyContent] =>
    appUserDal.isUserEligibleForRegistration(clientId, employeeId)
      .map(result => Ok(s"$result"))
      .recover(t => InternalServerError(t.getMessage))
  }
}
