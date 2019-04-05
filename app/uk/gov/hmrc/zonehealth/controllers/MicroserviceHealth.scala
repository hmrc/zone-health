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

package uk.gov.hmrc.zonehealth.controllers

import akka.util.ByteString
import com.google.inject.Inject
import play.api.http.HttpEntity
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.controller.BaseController
import uk.gov.hmrc.zonehealth.service.ZoneHealthService
import scala.concurrent.ExecutionContext


class MicroserviceHealth @Inject() (zoneHealthService: ZoneHealthService)(implicit executionContext: ExecutionContext) extends BaseController {

	def health() = Action.async { implicit request =>
    zoneHealthService.checkHealth().map {
      case Right(_) => Results.Ok
      case Left(e)  => Results.BadGateway.copy(body = HttpEntity.Strict(ByteString(e.getBytes), None))
    }
	}
}
