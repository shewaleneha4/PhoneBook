import com.typesafe.config.ConfigFactory

organization in ThisBuild := "com.squareone"
version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.12.4"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test
val jsonExtensions = "ai.x" %% "play-json-extensions" % "0.10.0"
val joda = "joda-time" % "joda-time" % "2.9.9"
val playJsonJoda = "com.typesafe.play" %% "play-json-joda" % "2.6.0-RC1"
val consulApi = "com.ecwid.consul" % "consul-api" % "1.4.0"
val lagomConsul = "lagom-service-locator-scaladsl-consul" %% "lagom-service-locator-scaladsl-consul" % "1.4.0-SNAPSHOT"

lazy val `phoneBook` = (project in file("."))
  .aggregate(
    `phonebook-api`, `phonebook-impl`)

lazy val `common` = (project in file("common"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      lagomScaladslServer,
      lagomScaladslPersistenceCassandra
    )
  )

lazy val `phonebook-api` = (project in file("phonebook-api"))
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslApi,
      playJsonJoda,
      jsonExtensions
    )
  ).dependsOn(`common`)

lazy val `phonebook-impl` = (project in file("phonebook-impl"))
  .enablePlugins(LagomScala)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      macwire,
      scalaTest,
      joda,
      consulApi,
      lagomConsul,
      guice
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`phonebook-api`)

lagomCassandraEnabled in ThisBuild := false
lagomKafkaEnabled in ThisBuild := false


lagomUnmanagedServices in ThisBuild := Map(
  "cas_native" -> ConfigFactory.load().getString("cassandra.url")
)