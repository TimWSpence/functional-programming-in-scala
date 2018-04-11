import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.3"
  lazy val cats = "org.typelevel" %% "cats-core" % "1.0.1"
  lazy val shapeless = "com.chuusai" %% "shapeless" % "2.3.3"
  lazy val akka = "com.typesafe.akka" %% "akka-actor" % "2.5.9"
  lazy val akkaStreams = "com.typesafe.akka" %% "akka-stream" % "2.5.9"
}
