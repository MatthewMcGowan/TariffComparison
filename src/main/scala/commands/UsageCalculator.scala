package commands

import models.{FuelType, Gas, Power, Tariff}

/**
  * Created by Matt on 20/05/2017.
  */
class UsageCalculator {
  /** Returns the annual consumption.
    * If the tariff does not have a rate for the given fuel type, will return None.
    */
  def usage(tariff: Tariff, fuel: FuelType, spend: BigDecimal, vatRate: BigDecimal): Option[BigDecimal] = {
    ???
  }
}
