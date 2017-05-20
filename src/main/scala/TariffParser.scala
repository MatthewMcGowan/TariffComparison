import models.{Rates, Tariff}
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

import scala.util.Try

/**
  * Created by Matt on 20/05/2017.
  *
  * Parses Tariff JSON.
  */
class TariffParser {

  implicit val rateReads: Reads[Rates] = (
    (__ \ "power").readNullable[Double] and
      (__ \ "gas").readNullable[Double]
    )(Rates.apply _)

  implicit val tariffReads: Reads[Tariff] = (
    (__ \ "tariff").read[String] and
      (__ \ "rates").read[Rates] and
      (__ \ "standing_charge").read[Double]
    )(Tariff.apply _)


  def parse(json: String): Option[Seq[Tariff]] = {
    val j = Try(Json.parse(json)).toOption

    // We flatMap and lose information as to _why_ the operation fails.
    // Invalid JSON? Or did it not conform to schema? TODO: Log.
    j.flatMap(_.validate[Seq[Tariff]].asOpt)
  }
}
