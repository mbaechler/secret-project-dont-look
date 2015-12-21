name := """xteam-exam-http"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.apache.httpcomponents" % "httpclient" % "4.5.1",
  "com.codepoetics" % "protonpack" % "1.7",
  "com.jayway.restassured" % "rest-assured" % "2.8.0" % Test,
  "org.mockito" % "mockito-core" % "1.10.19" % Test
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
