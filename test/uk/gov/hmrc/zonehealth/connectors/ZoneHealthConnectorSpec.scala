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

package uk.gov.hmrc.zonehealth.connectors

import play.api.test.{FakeApplication, WithApplication}
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.zonehealth.WireMockEndpoints

import scala.concurrent.Future

class ZoneHealthConnectorSpec extends UnitSpec with WireMockEndpoints {

  "ZoneHealthConnector" should {
    "not try to connect to downstream when none is configured" in {
      val conn = new ZoneHealthConnector(){
        override def httpGetStatus: (String) => Future[Int] = ???
        override def zoneHealthDownstreamUrl: Option[String] = None
      }

      conn.maybeCheckDownstreamHealth() shouldBe None
    }

    "return Some(500) when downstream is configured and returns 500" in {
      zoneHealthProtectedEndpointResponse((500, None))

      val conn = new ZoneHealthConnector(){
        override def httpGetStatus = (s) => Future.successful(500)
        override def zoneHealthDownstreamUrl: Option[String] = Some("")
      }

      status(conn.maybeCheckDownstreamHealth().get) shouldBe 500
    }

    "return Some(500) when downstream is configured and the call fails" ignore {
      zoneHealthProtectedEndpointResponse((500, None))

      val conn = new ZoneHealthConnector(){
        override def httpGetStatus = (s) => Future.failed(new RuntimeException("failed"))
        override def zoneHealthDownstreamUrl: Option[String] = Some("")
      }

      status(conn.maybeCheckDownstreamHealth().get) shouldBe 500
    }

    "return Some(200) when downstream is configured and returns 200" in new WithApplication(FakeApplication()){
      zoneHealthProtectedEndpointResponse((200, None))

      val conn = new ZoneHealthConnector(){
        override def httpGetStatus = (s) => Future.successful(200)
        override def zoneHealthDownstreamUrl: Option[String] = Some(s"")
      }

      status(conn.maybeCheckDownstreamHealth().get) shouldBe 200
    }
  }
}
