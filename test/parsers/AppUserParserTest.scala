package parsers

import org.scalatest.{FunSpec, MustMatchers}

class AppUserParserTest extends FunSpec with MustMatchers {

  describe("create") {
    it("should create a new parser if given client id is supported") {
      val supportedClientId = 1

      val result = AppUserParser.create(supportedClientId)

      result.isSuccess mustEqual true
      result.get.isInstanceOf[Client1AppUserParser] mustEqual true
    }

    it("should fail if given client id is not supported") {
      val supportedClientId = 3

      val result = AppUserParser.create(supportedClientId)

      result.isFailure mustEqual true
      result.failed.get.isInstanceOf[UnsupportedOperationException] mustEqual true
    }
  }
}
