import models.{Rate, Tariff}
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

/**
  * Created by Matt on 20/05/2017.
  *
  * Parses Tariff JSON.
  */
class TariffParser {

  implicit val rateReads: Reads[Rate] = (
    (__ \ "power").readNullable[Double] and
      (__ \ "gas").readNullable[Double]
    )(Rate.apply _)

  implicit val tariffReads: Reads[Tariff] = (
    (__ \ "tariff").read[String] and
      (__ \ "rates").read[Rate] and
      (__ \ "standing_charge").read[Double]
    )(Tariff.apply _)


  def parse(json: String): Option[Seq[Tariff]] = {
    val j = Json.parse(json)

    j.validate[Seq[Tariff]].asOpt
  }
}
