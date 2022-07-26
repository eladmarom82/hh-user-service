package parsers

import java.sql.Date
import scala.util.matching.Regex
import scala.util.{Failure, Try}

class Client1UserParser extends UserParser {
  override protected val clientId: Int = 1
  override protected val pattern: Regex = raw"(\S+),(\S+),(\d{4})-(\d{2})-(\d{2}),(\d+)".r

  override def parse(row: String): Try[UserRecord] = {
    row match {
      case pattern(firstName, lastName, year, month, day, userId) =>
        Try(UserRecord(firstName, lastName, new Date(year.toInt, month.toInt, day.toInt), clientId, userId.toInt))
      case _ => Failure(new IllegalArgumentException("wrong row format"))
    }
  }
}
