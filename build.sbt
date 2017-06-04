name := "actorstage"

version := "1.0"

organization := "com.keenworks"

scalaVersion := "2.11.11"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.1"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.2"

mainClass in (Compile,run) := Some("com.keenworks.sample.sampleactorstage.SampleActorStage")
