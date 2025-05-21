import play.core.PlayVersion
import play.sbt.PlayImport
import sbt._

object AppDependencies {

  val hmrcBootstrapVersion = "9.12.0"
  val hmrcMongoVersion     = "2.6.0"
  val mockitoVersion       = "1.17.30"
  val flexmarkVersion      = "0.64.8"

  val compile = Seq(
    "uk.gov.hmrc"       %% "bootstrap-backend-play-30" % hmrcBootstrapVersion,
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-30"        % hmrcMongoVersion,
    PlayImport.ws
  )

  val test = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-30"   % hmrcBootstrapVersion % Test,
    "com.vladsch.flexmark"   %  "flexmark-all"             % flexmarkVersion      % Test,
    "org.mockito"            %% "mockito-scala-scalatest"  % mockitoVersion       % Test
  )

  val itTest = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-30"   % hmrcBootstrapVersion % "it",
    "uk.gov.hmrc.mongo"      %% "hmrc-mongo-test-play-30"  % hmrcMongoVersion     % "it",
    "com.vladsch.flexmark"   %  "flexmark-all"             % flexmarkVersion      % "it",
    "org.mockito"            %% "mockito-scala-scalatest"  % mockitoVersion       % "it",
    "com.dimafeng"           %% "testcontainers-scala"     % "0.39.5"             % "it"
  )
}
