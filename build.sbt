name := "akka_streams_basic"

version := "1.0"

scalaVersion := "2.11.8"

val projectMainClass = "org.kajjoy.akka.streams.basic.Application"

val akkaVersion = "2.4.10"

mainClass in (Compile, run) := Some(projectMainClass)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-agent" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion
)