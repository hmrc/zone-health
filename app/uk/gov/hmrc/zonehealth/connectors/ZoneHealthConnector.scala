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

package uk.gov.hmrc.zonehealth.connectors

import play.api.libs.ws.WSClient
import play.api.mvc.Results
import play.api.mvc.Results._

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ZoneHealthConnector @Inject()(
 zoneHealthDownstream: ZoneHealthDownstream,
 downstream: Option[DownstreamInstance]
)(implicit
  ec: ExecutionContext
){

  def maybeCheckDownstreamHealth(): Option[Future[Results.Status]] = {
    downstream map { downstreamInstance =>
      zoneHealthDownstream.httpGetStatus(downstreamInstance.url).map {
        case s if s == 200 => Ok
        case _ => InternalServerError
      }
    }
  }
}

@Singleton
class ZoneHealthDownstream @Inject()(ws: WSClient)(implicit ec: ExecutionContext) {
  def httpGetStatus(url:String): Future[Int] = ws.url(url).get().map(_.status)
}

case class DownstreamInstance(url: String)
