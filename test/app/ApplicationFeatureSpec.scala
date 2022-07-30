package app

import akka.actor.ActorSystem
import org.scalatest.GivenWhenThen
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.mvc.Result
import play.api.test.Helpers.{route, _}
import play.api.test._

/**
 * testing the application end to end using h2 in memory db
 */
class ApplicationFeatureSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with GivenWhenThen {

  trait TestContext {
    implicit val system: ActorSystem = app.injector.instanceOf(classOf[ActorSystem])

    def performGet(routeStr: String): Result = await(route(app, FakeRequest(GET, routeStr)).get)
    def performGetAndGetResultContent(routeStr: String): String = contentAsString(route(app, FakeRequest(GET, routeStr)).get)
  }

  "test application" should {
    "run scenario: user added in the following snapshot" in new TestContext {
      Given("(cid=2,eid=16) is not eligible")
      performGet("/init")
      performGet("/pull/2/20220727")
      performGetAndGetResultContent("/check?cid=2&eid=16") mustEqual "false"

      When("(cid=2,eid=16) is added in the next snapshot")
      performGet("/pull/2/20220728")

      Then("(cid=2,eid=16) must now be eligible")
      performGetAndGetResultContent("/check?cid=2&eid=16") mustEqual "true"
    }

    "run scenario: user still exists in the following snapshot" in new TestContext {
      Given("(cid=2,eid=12) is eligible")
      performGet("/init")
      performGet("/pull/2/20220727")
      performGetAndGetResultContent("/check?cid=2&eid=12") mustEqual "true"

      When("(cid=2,eid=12) exists in the next snapshot")
      performGet("/pull/2/20220728")

      Then("(cid=2,eid=12) must still be eligible")
      performGetAndGetResultContent("/check?cid=2&eid=12") mustEqual "true"
    }

    "run scenario: user removed in the following snapshot" in new TestContext {
      Given("(cid=2,eid=13) is eligible")
      performGet("/init")
      performGet("/pull/2/20220727")
      performGetAndGetResultContent("/check?cid=2&eid=13") mustEqual "true"

      When("(cid=2,eid=13) is removed from the next snapshot")
      performGet("/pull/2/20220728")

      Then("(cid=2,eid=13) must not be eligible anymore")
      performGetAndGetResultContent("/check?cid=2&eid=13") mustEqual "false"
    }
  }

}
