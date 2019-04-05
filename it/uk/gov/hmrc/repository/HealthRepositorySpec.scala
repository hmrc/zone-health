package uk.gov.hmrc.repository

import java.io.File

import com.dimafeng.testcontainers.{Container, DockerComposeContainer, ExposedService, ForAllTestContainer, GenericContainer}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import uk.gov.hmrc.zonehealth.repository.MongoZoneHealthRepository

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class HealthRepositorySpec extends PlaySpec with GuiceOneServerPerSuite with BeforeAndAfterEach with ScalaFutures
  with IntegrationPatience {
  // These tests require a local instance of Mongo. For convenience, when running locally the three lines of code below
  // can be uncommented in order start a docker container running mongo. It's commented out as there is no support for
  // starting docker containers within Jenkins. Alternatively run `docker-compose up` within the it/resources folder to
  // start a container manually.


//  with ForAllTestContainer  {
//  override val container: Container = DockerComposeContainer(new File(dockerComposePath),
//    exposedServices = Seq(ExposedService("mongo", 27017)))
  val healthRepository = app.injector.instanceOf[MongoZoneHealthRepository]

  override def beforeEach() {
    Await.result(healthRepository.drop, 1.second)
  }

  "get" should {
    "not find the token when it hasn't been added" in {
      Await.result(healthRepository.tokenExists(), 1.second) must be(false)
    }

    "find the token when it has been added" in {
      Await.result(healthRepository.putToken(), 1.second)
      Await.result(healthRepository.tokenExists(), 1.second) must be(true)
    }
  }
  private val dockerComposePath: String = getClass.getResource("/docker-compose.yml").getPath
}

