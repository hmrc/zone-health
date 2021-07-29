import play.core.PlayVersion
import play.sbt.PlayImport
import sbt._

object AppDependencies {

  val hmrcBootstrapVersion = "5.7.0"
  val hmrcMongoVersion     = "0.51.0"

  val compile = Seq(
    "uk.gov.hmrc"       %% "bootstrap-backend-play-28" % hmrcBootstrapVersion,
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-28"        % hmrcMongoVersion,
    "com.iheart"        %% "ficus"                     % "1.4.5",
    PlayImport.ws
  )

  val test = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-28"   % hmrcBootstrapVersion % Test,
    "com.vladsch.flexmark"   %  "flexmark-all"             % "0.35.10"            % Test,
    "org.mockito"            %% "mockito-scala-scalatest"  % "1.16.23"            % Test
  )

  val itTest = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-28"   % hmrcBootstrapVersion % "it",
    "uk.gov.hmrc.mongo"      %% "hmrc-mongo-test-play-28"  % hmrcMongoVersion     % "it",
    "com.vladsch.flexmark"   %  "flexmark-all"             % "0.35.10"            % "it",
    "org.mockito"            %% "mockito-scala-scalatest"  % "1.16.23"            % "it",
    "com.dimafeng"           %% "testcontainers-scala"     % "0.39.5"             % "it"
  )
}
