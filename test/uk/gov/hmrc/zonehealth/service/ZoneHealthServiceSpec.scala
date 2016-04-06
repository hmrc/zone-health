/*
 * Copyright 2016 HM Revenue & Customs
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

package uk.gov.hmrc.zonehealth.service

import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.zonehealth.connectors.ZoneHealthConnector
import uk.gov.hmrc.zonehealth.repository.ZoneHealthRepository

import scala.concurrent.Future

class ZoneHealthServiceSpec extends UnitSpec {

  "ZoneHealthService" should {

    "return Right when a dependent service is OK and mongo-health has returned correctly" in {
      val zoneHealthService = ZoneHealthServiceBuilder(
        downstreamStatus = Future.successful(200),
        mongoPutTokenResult = Future.successful(),
        mongoTokenExists = Future.successful(true)
      ).build()

      await(zoneHealthService.checkHealth()) shouldBe Right()
    }

    "return Left when a dependent service has Failed and mongo-health has returned correctly" in {
      val zoneHealthService = ZoneHealthServiceBuilder(
        downstreamStatus = Future.successful(500),
        mongoPutTokenResult = Future.successful(),
        mongoTokenExists = Future.successful(true)
      ).build()

      await(zoneHealthService.checkHealth()) shouldBe Left("downstream returned 500")
    }

    "return Left when a dependent service is available and mongo-health has failed to write" in {
      val MongoErrorMessage = "Mongo Error"

      val zoneHealthService = ZoneHealthServiceBuilder(
        downstreamStatus = Future.successful(200),
        mongoPutTokenResult = Future.failed(new Exception(MongoErrorMessage)),
        mongoTokenExists = Future.successful(true)
      ).build()

      await(zoneHealthService.checkHealth()) shouldBe Left("exception getting zone health: 'Mongo Error'")
    }

    "return Left when a dependent service is available and mongo-health has failed to read back the token" in {
      val zoneHealthService = ZoneHealthServiceBuilder(
        downstreamStatus = Future.successful(200),
        mongoPutTokenResult = Future.successful(),
        mongoTokenExists = Future.successful(false)
      ).build()

      await(zoneHealthService.checkHealth()) shouldBe Left("mongo is unavailable")
    }

    "return Left when a dependent service is available and mongo-health got an exception when reading back the token" in {
      val MongoErrorMessage = "Mongo Error"

      val zoneHealthService = ZoneHealthServiceBuilder(
        downstreamStatus = Future.successful(200),
        mongoPutTokenResult = Future.successful(),
        mongoTokenExists = Future.failed(new Exception(MongoErrorMessage))
      ).build()

      await(zoneHealthService.checkHealth()) shouldBe Left("exception getting zone health: 'Mongo Error'")
    }
  }
}


case class ZoneHealthServiceBuilder(
                                     downstreamUrl:Option[String] = Some("zone-health-url"),
                                     downstreamStatus:Future[Int] = Future.successful(200),
                                     mongoPutTokenResult: Future[Unit] = Future.successful(),
                                     mongoTokenExists: Future[Boolean] = Future.successful(true)
                                   ){
  def build():ZoneHealthService = new ZoneHealthService {
    override def downstreamConnector = new ZoneHealthConnector{
      override def zoneHealthDownstreamUrl: Option[String] = downstreamUrl
      override def httpGetStatus: (String) => Future[Int] = (s) => downstreamStatus
    }

    override def mongoRepository = new ZoneHealthRepository{
      override def tokenExists(): Future[Boolean] = mongoTokenExists
      override def putToken(): Future[Unit] = mongoPutTokenResult
    }
  }
}
