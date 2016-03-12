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

package uk.gov.hmrc.zonehealth.controllers

import org.scalatest.{BeforeAndAfter, Tag, TestData}
import org.scalatestplus.play.OneServerPerTest
import play.api.test.FakeApplication
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.zonehealth.WireMockEndpoints

sealed case class ZoneType(t:String) extends Tag(t)
object Public extends ZoneType("public")
object Protected extends ZoneType("protected")
object Private extends ZoneType("private")

class MicroserviceHealthControllerSpec extends UnitSpec with BeforeAndAfter with OneServerPerTest with WireMockEndpoints {

  override def newAppForTest(testData: TestData): FakeApplication = {
    new FakeApplication(additionalConfiguration = Map(
      "zone" -> (if(testData.tags.contains(Protected.name)) "protected" else "public"),
      "microservice.services.zone-health-protected.port" -> endpointPort
    ))
  }

  "GET /zone-health in 'protected' zone" should {
      "return 200" taggedAs Protected in {
        val result = route(FakeRequest(GET, "/zone-health"))
        result.isDefined should be(true)
        await(status(result.get)) should be (OK)
      }
  }

  "GET /zone-health in 'public' zone WITHOUT a connection to a protected instance" should {
    "return 500" in {
      val result = route(FakeRequest(GET, "/zone-health"))
      result.isDefined should be(true)
      await(status(result.get)) should be (INTERNAL_SERVER_ERROR)
    }
  }

  "GET /zone-health in 'public' zone WITH a connection to a protected instance" should {
    "return 200" in {
      zoneHealthProtectedEndpointResponse((200, None))
      val result = route(FakeRequest(GET, "/zone-health"))
      result.isDefined should be(true)
      await(status(result.get)) should be (OK)
    }
  }

  "GET /zone-health/x" should {
    "be undefined" in {
      val result = route(FakeRequest(GET, "/zone-health/x"))
      result.isDefined should be(false)
    }
  }
}
