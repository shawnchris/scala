lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.11.8",
  scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation"),
  resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.3",
  libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.4.3"
)

lazy val root = (project in file(".")).
  aggregate(server, client)

lazy val server = (project in file("server")).
  settings(commonSettings: _*).
  settings(
  name := "Server 2"
)

lazy val client = (project in file("client")).
  settings(commonSettings: _*).
  settings(
  name := "Client 2"
)

