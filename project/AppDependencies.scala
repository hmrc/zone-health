import play.core.PlayVersion
import play.sbt.PlayImport
import sbt._

object AppDependencies {

  val hmrcMongoVersion = "0.50.0"

  val compile = Seq(
    "uk.gov.hmrc"       %% "bootstrap-backend-play-28" % "5.7.0",
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-28"        % hmrcMongoVersion,
    "uk.gov.hmrc"       %% "simple-reactivemongo"      % "8.0.0-play-28",
    "com.iheart"        %% "ficus"                     % "1.4.5",
    PlayImport.ws
  )

  val test = Seq(
    "org.scalatest"          %% "scalatest"                % "3.2.6"             % Test,
    "com.vladsch.flexmark"   %  "flexmark-all"             % "0.35.10"           % Test,
    "com.typesafe.play"      %% "play-test"                % PlayVersion.current % Test,
    "uk.gov.hmrc.mongo"      %% "hmrc-mongo-test-play-28"  % hmrcMongoVersion    % Test,
    "uk.gov.hmrc"            %% "reactivemongo-test"       % "5.0.0-play-28"     % Test,
    "org.scalatestplus.play" %% "scalatestplus-play"       % "5.1.0"             % Test,
    "org.mockito"            % "mockito-core"              % "3.11.2"            % Test
  )

  val itTest = test ++ Seq(
    "com.typesafe.play"      %% "play-test"                % PlayVersion.current % "it",
    "org.scalatest"          %% "scalatest"                % "3.2.6"             % "it",
    "org.pegdown"            % "pegdown"                   % "1.6.0"             % "it",
    "uk.gov.hmrc"            %% "reactivemongo-test"       % "5.0.0-play-28"     % "it",
    "org.scalatestplus.play" %% "scalatestplus-play"       % "5.1.0"             % "it",
//    "com.github.tomakehurst" % "wiremock"                  % "2.72.2"            % "it",
    "org.mockito"            % "mockito-core"              % "3.11.2"            % "it",
    "com.dimafeng"           %% "testcontainers-scala"     % "0.39.5"            % "it"
  )
}
