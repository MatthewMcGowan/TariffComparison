package models

/**
  * Created by Matt on 14/05/2017.
  */
sealed trait FuelType
object FuelType {
  def fromString(s: String): Option[FuelType] = s match {
    case "power" => Some(Power)
    case "gas" => Some(Gas)
    case _ => None
  }
}
case object Power extends FuelType
case object Gas extends FuelType