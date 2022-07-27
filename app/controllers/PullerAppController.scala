package controllers

import play.api.mvc._
import puller.PullerApp

import javax.inject._

/**
 * This controller represents the component that pulls new files from the different clients repositories.
 * It can also be a simple scala app (main), but its here for your convenience in checking the process.
 * assuming every client file will have a timestamp, if not attached to the filename, then the creation date for example.
 */
@Singleton
class PullerAppController @Inject()(pullerApp: PullerApp) extends InjectedController {

  def pull(clientId: Int, yyyyMMdd: Int) = Action { implicit request: Request[AnyContent] =>
    val numLines = pullerApp.pull(clientId, yyyyMMdd)

    Ok(s"$numLines")
  }
}
