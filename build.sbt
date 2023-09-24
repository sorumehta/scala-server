
lazy val rootProject = (project in file(".")).enablePlugins(SbtTwirl).settings(
  Seq(
    name := "tapir-loom-server",
    version := "0.1.0-SNAPSHOT",
    organization := "com.app",
    scalaVersion := "3.3.1",
    javaOptions += "--enable-preview",
    fork := true,
    libraryDependencies ++= Seq(
      // tapir and server
      "com.softwaremill.sttp.tapir" %% "tapir-netty-server-id" % "0.2.4",
      "com.softwaremill.sttp.tapir" %% "tapir-files" % "1.7.4",
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % "1.7.3",
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "1.7.3",

      // security
      "com.password4j" % "password4j" % "1.7.3",
      "com.auth0" % "java-jwt" % "4.4.0",

      // db
      "org.postgresql" % "postgresql" % "42.5.4",
      "io.getquill" %% "quill-jdbc" % "4.6.0.1",
      "org.flywaydb" % "flyway-core" % "9.22.2",
      "com.zaxxer" % "HikariCP" % "5.0.1",

      //logging
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
      "ch.qos.logback" % "logback-classic" % "1.4.11",

      // config
      "com.typesafe" % "config" % "1.4.2"
    )
  )
)
