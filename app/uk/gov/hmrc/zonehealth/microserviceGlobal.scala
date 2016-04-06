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

package uk.gov.hmrc.zonehealth

import java.util.concurrent.TimeUnit

import com.typesafe.config.Config
import play.api.{Application, Configuration, Logger, Play}
import uk.gov.hmrc.play.audit.filters.AuditFilter
import uk.gov.hmrc.play.config.{AppName, ControllerConfig, RunMode}
import uk.gov.hmrc.play.http.logging.filters.LoggingFilter
import uk.gov.hmrc.play.microservice.bootstrap.DefaultMicroserviceGlobal
import net.ceedubs.ficus.Ficus._
import uk.gov.hmrc.zonehealth.repository.ZoneHealthRepository

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}


object ControllerConfiguration extends ControllerConfig {
  lazy val controllerConfigs = Play.current.configuration.underlying.as[Config]("controllers")
}

object MicroserviceAuditFilter extends AuditFilter with AppName {
  override val auditConnector = MicroserviceAuditConnector
  override def controllerNeedsAuditing(controllerName: String) = ControllerConfiguration.paramsForController(controllerName).needsAuditing
}

object MicroserviceLoggingFilter extends LoggingFilter {
  override def controllerNeedsLogging(controllerName: String) = ControllerConfiguration.paramsForController(controllerName).needsLogging
}

object MicroserviceGlobal extends DefaultMicroserviceGlobal with RunMode {
  override val auditConnector = MicroserviceAuditConnector
  override def microserviceMetricsConfig(implicit app: Application): Option[Configuration] = app.configuration.getConfig(s"microservice.metrics")
  override val loggingFilter = MicroserviceLoggingFilter
  override val microserviceAuditFilter = MicroserviceAuditFilter
  override val authFilter = None

//  override def onStart(app: Application) {
//
//    import scala.concurrent.ExecutionContext.Implicits.global
//
//    Logger.info("Zone-Health starting up, will put initial token into mongo...")
//
//    Future {
//      Thread.sleep(10000)
//      val putF = HealthRepository.apply().putToken()
//
//      Await.ready(putF, Duration(10, TimeUnit.SECONDS))
//
//      putF.onComplete {
//        case Success(s) => {
//          Logger.info("initial token successfully inserted into Mongo, continuing startup")
//          super.onStart(app)
//        }
//        case Failure(e) => {
//          Logger.error("failed to insert token into Mongo, halting service with System.exit")
//          System.exit(-1)
//        }
//      }
//    }
//  }
}
