import uk.gov.hmrc.DefaultBuildSettings

val silencerVersion = "1.7.3"

lazy val microservice = Project("zone-health", file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(
    scalaVersion        := "2.12.13",
    majorVersion        := 0,
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test ++ AppDependencies.itTest
  )
  .settings(SbtDistributablesPlugin.publishingSettings: _*)
  .configs(IntegrationTest)
  .settings(DefaultBuildSettings.integrationTestSettings(): _*)
  .settings(resolvers += Resolver.jcenterRepo)
  .settings(
      // Use the silencer plugin to suppress warnings from unused imports in routes
      scalacOptions += "-P:silencer:pathFilters=routes",
      libraryDependencies ++= Seq(
          compilerPlugin("com.github.ghik" % "silencer-plugin" % silencerVersion cross CrossVersion.full),
          "com.github.ghik" % "silencer-lib" % silencerVersion % Provided cross CrossVersion.full
      )
  )
