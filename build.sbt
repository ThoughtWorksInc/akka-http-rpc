organization in ThisBuild := "com.thoughtworks.akka-http-rpc"

crossScalaVersions in ThisBuild := Seq("2.10.6", "2.11.8")

libraryDependencies += "com.typesafe.akka" %% "akka-http-experimental" % "2.0.4"

libraryDependencies += "com.lihaoyi" %% "upickle" % "0.4.1"

libraryDependencies += "com.lihaoyi" %% "autowire" % "0.2.5"

libraryDependencies += "com.thoughtworks.extractor" %% "extractor" % "latest.release"
