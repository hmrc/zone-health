/*
 * Copyright 2019 HM Revenue & Customs
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

import org.scalatest._
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import play.api.mvc.Result

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._


class ZoneHealthConnectorSpec extends FlatSpec with Matchers with MockitoSugar {

  "ZoneHealthConnector" should
    "not try to connect to downstream when none is configured" in {
      val conn = new ZoneHealthConnector(mock[ZoneHealthDownstream], None)

      conn.maybeCheckDownstreamHealth() shouldBe None
    }

    it should "return Some(500) when downstream is configured and returns 500" in {
      val downstream = mock[ZoneHealthDownstream]

      when(downstream.httpGetStatus("foo")).thenReturn(Future.successful(500))
      val conn = new ZoneHealthConnector(downstream, Some(DownstreamInstance("foo")))

      conn.maybeCheckDownstreamHealth().get map {r => r.header.status shouldBe 500}
    }

    it should "return Some(500) when downstream is configured and the call fails" in {
      val downstream = mock[ZoneHealthDownstream]
      when(downstream.httpGetStatus("foo")).thenReturn(Future.failed(new RuntimeException("failed")))

      val conn = new ZoneHealthConnector(downstream, Some(DownstreamInstance("foo")))

      conn.maybeCheckDownstreamHealth().get map {r => r.header.status shouldBe 500}
    }

    it should "return Some(200) when downstream is configured and returns 200" in {
      val downstream = mock[ZoneHealthDownstream]

      when(downstream.httpGetStatus("foo")).thenReturn(Future.successful(200))
      val conn = new ZoneHealthConnector(downstream, Some(DownstreamInstance("foo")))

      conn.maybeCheckDownstreamHealth().get map {r => r.header.status shouldBe 200}
    }
}
