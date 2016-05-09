lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.11.8",
  scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation"),
  resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.3",
  libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.4.3"
)

lazy val root = (project in file(".")).
  aggregate(watchee, watcher)

lazy val watchee = (project in file("watchee")).
  settings(commonSettings: _*).
  settings(
  name := "Watchee"
)

lazy val watcher = (project in file("watcher")).
  settings(commonSettings: _*).
  settings(
  name := "Watcher"
)

