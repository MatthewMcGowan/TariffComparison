package commands

import models.Tariff

/**
  * Created by Matt on 20/05/2017.
  */
class CostCalculator {
  def costs(tariffs: Iterable[Tariff], powerUsage: Option[kWh], gasUsage: Option[kWh], vatRate: Percent):
  Iterable[(String, BigDecimal)] = {
    ???
  }

  type kWh = Int
  type Percent = BigDecimal
}
