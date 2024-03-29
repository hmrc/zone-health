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

package uk.gov.hmrc.zonehealth.repository

import javax.inject.{Inject, Singleton}
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.FindOneAndUpdateOptions
import org.mongodb.scala.model.Updates.set
import play.api.libs.json._
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import scala.concurrent.{ExecutionContext, Future}


case class ZoneToken(value:String = "1")

object ZoneToken {
  implicit val mongoFormats: Format[ZoneToken] = Json.format[ZoneToken]
  implicit val mongoOFormats: OFormat[ZoneToken] = Json.format[ZoneToken]
}

trait ZoneHealthRepository{
  def tokenExists(): Future[Boolean]
  def putToken(): Future[Unit]
}

@Singleton
class MongoZoneHealthRepository @Inject()(
 mongo: MongoComponent
)(implicit ec: ExecutionContext) extends PlayMongoRepository[ZoneToken](
    mongoComponent = mongo,
    collectionName = "zone-health",
    domainFormat = ZoneToken.mongoFormats,
    indexes = Seq()) with ZoneHealthRepository{


  private val TheZoneToken = ZoneToken("1")

  def tokenExists(): Future[Boolean] =
    collection
      .find(
        equal("value", TheZoneToken.value)
      )
      .headOption()
      .map(_.exists(_ == TheZoneToken))

  def putToken(): Future[Unit] = {
    collection
      .findOneAndUpdate(
        filter = equal("value", TheZoneToken.value),
        update = set("value", TheZoneToken.value),
        options = FindOneAndUpdateOptions().upsert(true)
      )
      .toFuture()
      .map(_ => ())
  }
}
