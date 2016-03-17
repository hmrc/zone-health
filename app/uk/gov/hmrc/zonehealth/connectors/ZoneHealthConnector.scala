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

import play.api.Play.current
import play.api.libs.ws.WS
import play.api.mvc.Results
import play.api.mvc.Results._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object ZoneHealthConnector extends ZoneHealthConnector{
  override val httpGetStatus = (s:String) => WS.url(s).get.map(_.status)
  override val zoneHealthDownstreamUrl:Option[String] = Config.baseUrlOpt(s"zone-health-downstream").map { ds =>
    s"$ds/zone-health"
  }
}

trait ZoneHealthConnector{

  def zoneHealthDownstreamUrl:Option[String]
  def httpGetStatus:(String) => Future[Int]

  def maybeCheckDownstreamHealth(): Option[Future[Results.Status]] = {
    zoneHealthDownstreamUrl map { url =>
      httpGetStatus(url).map {
        case s if s == 200 => Ok
        case _ => InternalServerError
      }
    }
  }

}
