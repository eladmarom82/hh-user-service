package parsers

import org.scalatest.{FunSpec, MustMatchers}

import java.sql.Date
import scala.util.Success

class Client1AppUserParserTest extends FunSpec with MustMatchers {

  describe("parse") {
    it("should return success when row is valid") {
      val validRow = "Lucy,Kennedy,Male,fuizifel@duoz.com,2016/04/27,1"

      val parser = new Client1AppUserParser

      val result = parser.parse(validRow)

      result mustEqual Success(AppUserRecord("Lucy", "Kennedy", new Date(2016, 4, 27), 1, 1))
    }

    it("should return failure when row is not in correct format") {
      val invalidRow = "eli,cohen,195-10-12,123"

      val parser = new Client1AppUserParser

      val result = parser.parse(invalidRow)

      result.isFailure mustBe true
      result.failed.get.isInstanceOf[IllegalArgumentException] mustBe true
    }
  }
}
