package parsers

import scala.util.Try
import scala.util.matching.Regex

trait AppUserParser {
  protected val clientId: Int
  protected val pattern: Regex

  def parse(row: String): Try[AppUserRecord]
}

object AppUserParser {
  val create: Map[Int, () => AppUserParser] = Map(
    1 -> (() => new Client1AppUserParser),
    2 -> (() => new Client2AppUserParser)
  )
}
