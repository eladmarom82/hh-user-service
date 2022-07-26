package module

import com.google.inject.{AbstractModule, Provides}
import pureconfig._
import pureconfig.generic.auto._

case class DbConfig(driver: String, url: String, user: String, password: String)

class AppModule extends AbstractModule {

  override def configure(): Unit = {
    super.configure()
  }

  @Provides
  def provideDbConfig(): DbConfig = {
    ConfigSource.defaultApplication.at("db").loadOrThrow[DbConfig]
  }
}
