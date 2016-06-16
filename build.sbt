name := """byrne"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.6"

resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/groups/staging/"
)

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  "fr.watchnext" % "utils_2.11" % "1.01",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc41",
  javaWs,
  evolutions
)

includeFilter in (Assets, LessKeys.less) := "*.less"

excludeFilter in (Assets, LessKeys.less) := "_*.less"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
