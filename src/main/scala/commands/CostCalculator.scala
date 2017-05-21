package commands

import models.Tariff

/**
  * Created by Matt on 20/05/2017.
  */
class CostCalculator {
  def costs(tariffs: Iterable[Tariff], powerUsage: Option[kWh], gasUsage: Option[kWh], vatRate: Percent):
  Seq[(String, BigDecimal)] = {
    def calc(t: Tariff): Option[(String, BigDecimal)] = {
      val standingCharge = 12 * t.standingCharge

      def energyCost(usage: Option[Int], rate: Option[BigDecimal]): Option[BigDecimal] = {
        // If used but no rate, not an applicable tariff
        if (usage.isDefined && rate.isEmpty)
          None
        // Otherwise, calculate
        else
          Some(
            (for(x <- usage; y <- rate) yield (x * y) + standingCharge)
              .getOrElse(0)) // Set to zero if no usage
      }

      def addVat(preVatCost: BigDecimal) = preVatCost * (1 + (vatRate / 100))

      val pCost = energyCost(powerUsage, t.rates.power).map(addVat)
      val gCost = energyCost(gasUsage, t.rates.gas).map(addVat)

      (for(x <- pCost; y <- gCost) yield x + y)
        .map((t.tariff, _))
    }

    tariffs
      .flatMap(calc)
      .toSeq // An ordered collection is not required (Iterable), but we do return one (Seq).
      .sortBy(x => x._2)
  }

  type kWh = Int
  type Percent = BigDecimal
}
