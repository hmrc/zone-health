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

package uk.gov.hmrc.zonehealth.controllers

import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.FakeRequest
import uk.gov.hmrc.zonehealth.repository.ZoneHealthRepository
import play.api.inject.bind
import org.mockito.Mockito._
import play.api.Application
import uk.gov.hmrc.zonehealth.service.ZoneHealthServiceBuilder

import scala.concurrent.Future
import org.scalatestplus.play.PlaySpec
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers._
import uk.gov.hmrc.play.bootstrap.tools.Stubs.stubMessagesControllerComponents

import scala.concurrent.ExecutionContext.Implicits.global

class ZoneHealthControllerSpec extends PlaySpec with GuiceOneAppPerSuite  with ScalaFutures with IntegrationPatience with MockitoSugar {

  private val zoneHealthRepo = mock[ZoneHealthRepository]

  val zoneHealthService = ZoneHealthServiceBuilder(
    downstreamStatus = Future.successful(200),
    mongoPutTokenResult = Future.successful(),
    mongoTokenExists = Future.successful(true)
  ).build()

  val mockMicroServiceHealthController = new MicroserviceHealth(stubMessagesControllerComponents(), zoneHealthService)

  val controller = mockMicroServiceHealthController

  when(zoneHealthRepo.putToken()).thenReturn(Future.successful(()))
  when(zoneHealthRepo.tokenExists()).thenReturn(Future.successful(true))
  override def fakeApplication(): Application = new GuiceApplicationBuilder()
    .overrides(bind[ZoneHealthRepository].to(zoneHealthRepo))
    .build()

  "GET /zone-health with no downstream to check" should {
    "return 200" in {
      val fakeRequest = FakeRequest(GET, "/zone-health")
      val result = controller.health()(fakeRequest)

      status(result) must be(OK)
    }
  }
}
