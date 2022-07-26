package parsers

import org.scalatest.{FunSpec, MustMatchers}

import java.sql.Date
import scala.util.Success

class Client1UserParserTest extends FunSpec with MustMatchers {

  describe("parse") {
    it("should return success when row is valid") {
      val validRow = "eli,cohen,1959-10-12,123"

      val parser = new Client1UserParser

      val result = parser.parse(validRow)

      result mustEqual Success(UserRecord("eli", "cohen", new Date(1959, 10, 12), 1, 123))
    }

    it("should return failure when row is not in correct format") {
      val invalidRow = "eli,cohen,195-10-12,123"

      val parser = new Client1UserParser

      val result = parser.parse(invalidRow)

      result.isFailure mustBe true
      result.failed.get.isInstanceOf[IllegalArgumentException] mustBe true
    }
  }
}
