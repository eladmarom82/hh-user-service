package parsers

import scala.util.Try
import scala.util.matching.Regex

trait UserParser {
  protected val clientId: Int
  protected val pattern: Regex

  def parse(row: String): Try[UserRecord]
}
