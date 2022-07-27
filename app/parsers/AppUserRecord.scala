package parsers

import java.sql.Date

case class AppUserRecord(firstName: String, lastName: String, birthDate: Date, clientId: Int, employeeId: Int)
