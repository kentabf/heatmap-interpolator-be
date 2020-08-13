name := "heatmap-interpolator"

version := "0.1"

scalaVersion := "2.13.3"

enablePlugins(JavaAppPackaging)

mainClass in Compile := Some("server.MyServer")

val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.0"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0",
  "org.scalanlp" %% "breeze" % "1.1",
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
)