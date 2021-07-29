/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.repository

import org.mongodb.scala.bson.BsonDocument
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import uk.gov.hmrc.zonehealth.repository.MongoZoneHealthRepository

import scala.concurrent.Await
import scala.concurrent.duration._

class HealthRepositorySpec extends PlaySpec with GuiceOneServerPerSuite with BeforeAndAfterEach with ScalaFutures
  with IntegrationPatience {
  // These tests require a local instance of Mongo. For convenience, when running locally the three lines of code below
  // can be uncommented in order start a docker container running mongo. It's commented out as there is no support for
  // starting docker containers within Jenkins. Alternatively run `docker-compose up` within the it/resources folder to
  // start a container manually.

//  with ForAllTestContainer  {
//
//  private val dockerComposePath: String = getClass.getResource("/docker-compose.yml").getPath
//
//  override val container: Container = DockerComposeContainer(new File(dockerComposePath),
//    exposedServices = Seq(ExposedService("mongo", 27017)))


  val healthRepository = app.injector.instanceOf[MongoZoneHealthRepository]

  override def beforeEach() {
    Await.result(healthRepository.collection.deleteMany(BsonDocument()).toFuture(), 1.second)
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
}

