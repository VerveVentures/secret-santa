ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "santa"
  )
  

val Http4sVersion = "1.0.0-M21"
val CirceVersion = "0.14.0-M5"
lazy val DoobieVersion = "1.0.0-RC2"
lazy val PureConfigVersion = "0.17.1"
lazy val LogbackVersion = "1.2.11"

libraryDependencies ++= Seq(
  "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s"      %% "http4s-blaze-client" % Http4sVersion,
  "org.http4s"      %% "http4s-circe"        % Http4sVersion,
  "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
  "io.circe"        %% "circe-generic"       % CirceVersion,
  "org.tpolecat"    %% "doobie-core" % DoobieVersion,
  "org.tpolecat"    %% "doobie-postgres" % DoobieVersion,
  "org.tpolecat"    %% "doobie-hikari" % DoobieVersion,
  "com.github.pureconfig" %% "pureconfig" % PureConfigVersion,
  "com.github.pureconfig" %% "pureconfig-cats-effect" % PureConfigVersion,
  "com.typesafe" % "config" % "1.2.0",
  "ch.qos.logback"        %  "logback-classic"      % LogbackVersion
)

enablePlugins(JavaAppPackaging)