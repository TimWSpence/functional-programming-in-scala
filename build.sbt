import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "org.timspence",
      scalaVersion := "2.12.3",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Functional Programming in Scala",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += cats,
    libraryDependencies += shapeless
    libraryDependencies += akka
    libraryDependencies += akkaStreams
  )

scalacOptions += "-Ypartial-unification"
