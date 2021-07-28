import play.core.PlayVersion
import play.sbt.PlayImport
import sbt._

object AppDependencies {

  val hmrcBootstrapVersion = "5.7.0"

  val compile = Seq(
    "uk.gov.hmrc"       %% "bootstrap-frontend-play-28" % hmrcBootstrapVersion,
    "uk.gov.hmrc"       %% "simple-reactivemongo"       % "8.0.0-play-28",
    "com.iheart"        %% "ficus"                      % "1.4.5",
    PlayImport.ws
  )

  val test = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-28"   % hmrcBootstrapVersion % Test,
    "com.vladsch.flexmark"   %  "flexmark-all"             % "0.35.10"            % Test,
    "org.mockito"            %% "mockito-scala-scalatest"  % "1.16.23"            % Test
  )

  val itTest = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-28"   % hmrcBootstrapVersion % "it",
    "com.vladsch.flexmark"   %  "flexmark-all"             % "0.35.10"            % "it",
    "uk.gov.hmrc"            %% "reactivemongo-test"       % "5.0.0-play-28"      % "it",
    "org.mockito"            %% "mockito-scala-scalatest"  % "1.16.23"            % "it",
    "com.dimafeng"           %% "testcontainers-scala"     % "0.39.5"             % "it"
  )
}
