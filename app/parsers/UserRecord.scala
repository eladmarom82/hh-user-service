package parsers

import java.sql.Date

case class UserRecord(firstName: String, lastName: String, birthDate: Date, clientId: Int, userId: Int)
