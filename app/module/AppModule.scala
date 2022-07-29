package module

import com.google.inject.{AbstractModule, Provides}
import play.api.Configuration

case class ProcessingConfig(batchSize: Int)

class AppModule extends AbstractModule {

  override def configure(): Unit = {
    super.configure()
  }

  @Provides
  def provideProcessingConfig(config: Configuration): ProcessingConfig =
    ProcessingConfig(config.get[Int]("processing.batch-size"))
}
