package parsers

import scala.util.{Failure, Try}
import scala.util.matching.Regex

trait AppUserParser {
  protected val clientId: Int
  protected val pattern: Regex

  def parse(row: String): Try[AppUserRecord]
}

object AppUserParser {
  private val parserMap: Map[Int, () => AppUserParser] = Map(
    1 -> (() => new Client1AppUserParser),
    2 -> (() => new Client2AppUserParser)
  )

  def create(clientId: Int): Try[AppUserParser] = Try(parserMap(clientId))
    .orElse(Failure(new UnsupportedOperationException(s"parser for clientId $clientId is not supported yet")))
    .map(_.apply)

}
