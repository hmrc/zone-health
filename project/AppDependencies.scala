import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt._

object AppDependencies {

  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "simple-reactivemongo" % "7.16.0-play-25",
    "uk.gov.hmrc" %% "bootstrap-play-25" % "4.10.0",
    "com.iheart" %% "ficus" % "1.4.5"
  )

  val test = Seq(
    "org.scalatest"     %% "scalatest"          % "3.0.5"             % "test",
    "com.typesafe.play" %% "play-test"          % PlayVersion.current % "test",
    "uk.gov.hmrc"       %% "reactivemongo-test" % "4.8.0-play-25"     % "test",
    "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test,
    "org.mockito" % "mockito-core" % "2.26.0" % Test
  )
  
  val itTest = test ++ Seq(
    "com.typesafe.play" %% "play-test" % PlayVersion.current % "it",
    "org.scalatest"     %% "scalatest"          % "3.0.5"             % "it",
    "org.pegdown"       % "pegdown"             % "1.6.0"             % "it",
    "com.typesafe.play" %% "play-test"          % PlayVersion.current % "it",
    "uk.gov.hmrc"       %% "reactivemongo-test" % "4.8.0-play-25"     % "it",
    "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % "it",
    "com.github.tomakehurst" % "wiremock" % "1.52" % "it",
    "org.mockito" % "mockito-core" % "2.26.0" % "it",
    "com.dimafeng" %% "testcontainers-scala" % "0.24.0" % "it"
  )
}
