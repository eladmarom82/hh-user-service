package parsers

import java.sql.Date
import scala.util.{Failure, Try}
import scala.util.matching.Regex

class Client2UserParser extends UserParser {
  override protected val clientId: Int = 2
  override protected val pattern: Regex = raw"(\d+),(\S+),(\S+),(\d{2})/(\d{2})/(\d{4})".r

  override def parse(row: String): Try[UserRecord] = {
    row match {
      case pattern(userId, firstName, lastName, day, month, year) =>
        Try(UserRecord(firstName, lastName, new Date(year.toInt, month.toInt, day.toInt), clientId, userId.toInt))
      case _ => Failure(new IllegalArgumentException("wrong row format"))
    }
  }
}
