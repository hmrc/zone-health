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

import org.scalatestplus.play.OneServerPerTest
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.play.test.UnitSpec


class ZoneHealthControllerSpec extends UnitSpec with OneServerPerTest {

  "GET /zone-health with no downstream to check" should {
    "return 200" in {
      val result = route(FakeRequest(GET, "/zone-health"))
      await(status(result.get)) should be (OK)
    }
  }

  "GET /zone-health/x" should {
    "be undefined" in {
      val result = route(FakeRequest(GET, "/zone-health/x"))
      result should be (None)
    }
  }
}
