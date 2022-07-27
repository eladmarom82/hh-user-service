package parsers

import org.scalatest.{FunSpec, MustMatchers}

import java.sql.Date
import scala.util.Success

class Client2AppUserParserTest extends FunSpec with MustMatchers {

  describe("parse") {
    it("should return success when row is valid") {
      val validRow = "12,tamar,zilber,10/12/1959"

      val parser = new Client2AppUserParser

      val result = parser.parse(validRow)

      result mustEqual Success(AppUserRecord("tamar", "zilber", new Date(1959, 12, 10), 2, 12))
    }

    it("should return failure when row is not in correct format") {
      val invalidRow = "12tamar,zilber,10/12/1959"

      val parser = new Client2AppUserParser

      val result = parser.parse(invalidRow)

      result.isFailure mustBe true
      result.failed.get.isInstanceOf[IllegalArgumentException] mustBe true
    }
  }
}
