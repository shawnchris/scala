lazy val root = (project in file(".")).
settings (
  name := "CSC536 Final Project",
  version := "1.0",
  scalaVersion := "2.11.8",
  resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.3",
  libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.4.3"
)


