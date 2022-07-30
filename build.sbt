name := """hh-user-service"""
organization := "com.hh"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .enablePlugins(DockerPlugin)

scalaVersion := "2.13.8"

resolvers += Resolver.typesafeRepo("releases")

libraryDependencies ++= Seq(
  jdbc,
  guice,
  "mysql" % "mysql-connector-java" % "5.1.47",
  "com.h2database" % "h2" % "2.1.214",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
)

Docker / version := "latest"
Docker / packageName := "hh-user-service"
Docker / maintainer := "marom.com"
Docker / daemonUser := "daemon"
dockerRepository := Some("https://hub.docker.com/r/eladmarom82/general")
dockerBaseImage := "java:8-jre-alpine"
dockerExposedPorts := Seq(9082)
dockerUpdateLatest := true

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.hh.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.hh.binders._"
