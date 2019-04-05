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

package uk.gov.hmrc.zonehealth.repository

import com.google.inject.Inject
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.play.json.ImplicitBSONHandlers._
import uk.gov.hmrc.mongo.ReactiveRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


case class ZoneToken(value:String = "1")

object ZoneToken {
  implicit val mongoFormats: Format[ZoneToken] = Json.format[ZoneToken]
  implicit val mongoOFormats: OFormat[ZoneToken] = Json.format[ZoneToken]
}

trait ZoneHealthRepository{
  def tokenExists(): Future[Boolean]
  def putToken(): Future[Unit]
}

class MongoZoneHealthRepository @Inject() (mongoComponent: ReactiveMongoComponent)
  extends ReactiveRepository[ZoneToken, String](
    "zone-health",
    mongoComponent.mongoConnector.db,
    ZoneToken.mongoFormats,
    implicitly[Format[String]]) with ZoneHealthRepository{


  private val TheZoneToken = ZoneToken("1")

  def tokenExists(): Future[Boolean] = find("value" -> JsString(TheZoneToken.value))
    .map(_.headOption.exists(_ == TheZoneToken))

  def putToken(): Future[Unit] = {
    import reactivemongo.bson.BSONDocument
    val selector = BSONDocument("value" -> TheZoneToken.value)
    collection.update(selector, TheZoneToken, upsert = true).map(_ => ())
  }
}
