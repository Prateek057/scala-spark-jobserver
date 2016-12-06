name := "scala-spark-jobserver"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(

  //Requiredfor Scheduler
  "com.typesafe.akka" %% "akka-actor" % "2.4.14",
  "com.typesafe.akka" %% "akka-agent" % "2.4.14",
  "org.apache.spark" %% "spark-core" % "1.6.1",
  "org.apache.spark" %% "spark-mllib" % "1.4.0"

)

resolvers += "Job Server Bintray" at "https://dl.bintray.com/spark-jobserver/maven"

libraryDependencies += "spark.jobserver" %% "job-server-api" % "0.6.2" % "provided"

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }