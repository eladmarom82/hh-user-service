package parsers

import scala.util.Try
import scala.util.matching.Regex

trait AppUserParser {
  protected val clientId: Int
  protected val pattern: Regex

  def parse(row: String): Try[AppUserRecord]
}
