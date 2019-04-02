organization in ThisBuild := "com.thoughtworks.akka-http-rpc"

libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.8"

libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.21" % Optional

libraryDependencies += "com.lihaoyi" %% "upickle" % "0.7.1"

libraryDependencies += "com.lihaoyi" %% "autowire" % "0.2.6"

libraryDependencies += "com.thoughtworks.extractor" %% "extractor" % "1.2.0"
