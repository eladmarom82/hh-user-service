package db

import play.api.db.Database

import javax.inject.Inject

class DbService @Inject() (db: Database) {
  def runCommand(command: String): Unit = {
    db.withConnection { connection =>
      val statement = connection.createStatement
      statement.execute(command)
    }
  }

//  def runQuery() = {
//    var connection: Connection = null
//    try {
//      Class.forName(dbConfig.driver)
//      connection = DriverManager.getConnection(dbConfig.url, dbConfig.user, dbConfig.password)
//      val statement = connection.createStatement
//      val rs = statement.executeQuery("SELECT host, user FROM user")
//      while (rs.next) {
//        val host = rs.getString("host")
//        val user = rs.getString("user")
//        println("host = %s, user = %s".format(host,user))
//      }
//    } catch {
//      case e: Exception => e.printStackTrace()
//    }
//    connection.close()
//  }

}
