resolvers += "twiiter repo" at "http://maven.twttr.com"

libraryDependencies ++= Seq(
   "com.twitter" % "util-eval" % "1.12.13" withSources(),
   "org.twitter4j" % "twitter4j-core" % "2.2.5",
   "org.twitter4j" % "twitter4j-stream" % "2.2.5"
)

scalacOptions += "-deprecation"
