package uk.gov.hmrc.repository

import org.scalatest.{BeforeAndAfterEach, OptionValues}
import reactivemongo.api.DB
import uk.gov.hmrc.mongo.MongoSpecSupport
import uk.gov.hmrc.zonehealth.repository.{HealthRepository, ZoneToken}
import uk.gov.hmrc.play.test.UnitSpec
import scala.concurrent.ExecutionContext.Implicits.global

class HealthRepositorySpec extends UnitSpec with BeforeAndAfterEach with OptionValues with MongoSpecSupport{

  def repository(implicit mongo: () => DB) = new HealthRepository

  val healthRepository = repository

  override def beforeEach() {
    await(healthRepository.drop)
  }

  println(databaseName)

  "get" should {

    "not find the token when it hasn't been added" in {
      await(healthRepository.tokenExists()) shouldBe false
    }

    "find the token when it has been added" in {
      await(healthRepository.putToken())
      await(healthRepository.tokenExists()) shouldBe true
    }
  }
}
