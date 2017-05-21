package commands

import models.{FuelType, Tariff}

/**
  * Created by Matt on 20/05/2017.
  */
class UsageCalculator {
  def usage(tariff: Tariff, fuel: FuelType, spend: BigDecimal, vatRate: BigDecimal):
  Option[BigDecimal] = {
    ???
  }
}
