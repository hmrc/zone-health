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

package uk.gov.hmrc.zonehealth.service

import com.google.inject.Inject
import play.api.Logger
import play.api.mvc.Results
import uk.gov.hmrc.zonehealth.connectors.ZoneHealthConnector
import uk.gov.hmrc.zonehealth.repository.ZoneHealthRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ZoneHealthService @Inject() (downstreamConnector: ZoneHealthConnector, mongoRepository: ZoneHealthRepository){

  val logger: Logger = Logger(this.getClass)

  def checkHealth(): Future[Either[String, Unit]] ={
    val result: Future[(Boolean, Either[String, Unit])] = for {
      downstream <- doDownStreamCheck()
      mongo <- doMongoCheck()
    } yield (mongo, downstream)

    (result map {
      case (true, e) => e
      case (false, Left(e)) => Left(s"mongo is unavailable, $e")
      case (false, Right(e)) => Left(s"mongo is unavailable")
    }).recover { case e =>
      logger.warn(s"exception getting zone health: '${e.getMessage}'", e)
      Left(s"exception getting zone health: '${e.getMessage}'")
    }
  }

  private def doDownStreamCheck():Future[Either[String, Unit]] = {
    downstreamConnector.maybeCheckDownstreamHealth().map { ft => ft
      .map {
        case Results.Ok => Right()
        case fail => {
          logger.warn(s"downstream returned ${fail.header.status}")
          Left(s"downstream returned ${fail.header.status}")
        }
      }
      .recover { case t => Left(t.getMessage) }
    }.getOrElse(Future.successful(Right()))
  }

  private def doMongoCheck(): Future[Boolean] = mongoRepository.putToken().flatMap { _ => mongoRepository.tokenExists() }
}
