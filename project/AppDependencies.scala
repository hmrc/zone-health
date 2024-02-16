import play.core.PlayVersion
import play.sbt.PlayImport
import sbt._

object AppDependencies {

  val hmrcBootstrapVersion = "8.4.0"
  val hmrcMongoVersion     = "1.7.0"
  val mockitoVersion       = "1.17.29"

  val compile = Seq(
    "uk.gov.hmrc"       %% "bootstrap-backend-play-30" % hmrcBootstrapVersion,
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-30"        % hmrcMongoVersion,
    PlayImport.ws
  )

  val test = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-30"   % hmrcBootstrapVersion % Test,
    "com.vladsch.flexmark"   %  "flexmark-all"             % "0.35.10"            % Test,
    "org.mockito"            %% "mockito-scala-scalatest"  % mockitoVersion       % Test
  )

  val itTest = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-30"   % hmrcBootstrapVersion % "it",
    "uk.gov.hmrc.mongo"      %% "hmrc-mongo-test-play-30"  % hmrcMongoVersion     % "it",
    "com.vladsch.flexmark"   %  "flexmark-all"             % "0.35.10"            % "it",
    "org.mockito"            %% "mockito-scala-scalatest"  % mockitoVersion       % "it",
    "com.dimafeng"           %% "testcontainers-scala"     % "0.39.5"             % "it"
  )
}
