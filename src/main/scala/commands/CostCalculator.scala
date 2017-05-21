package commands

import TariffCustomTypes._
import models.Tariff

/**
  * Created by Matt on 20/05/2017.
  */
class CostCalculator {
  val taxFuncs = new formulae.Tax
  val tarFuncs = new formulae.Tariffs

  /** Returns a set of tariff names with associated cost, ordered by ascending cost.
    * Will only return applicable tariffs for the given usage.
    */
  def costs(tariffs: Iterable[Tariff], powerUsage: Option[kWh], gasUsage: Option[kWh], vatRate: Percent):
  Seq[(String, BigDecimal)] = {

    def calc(t: Tariff): Option[(String, BigDecimal)] = {
      val standingCharge = tarFuncs.annualCost(t.standingCharge)

      def energyCost(usage: Option[Int], rate: Option[BigDecimal]): Option[BigDecimal] = {
        // If used but no rate, not an applicable tariff
        if (usage.isDefined && rate.isEmpty)
          None
        // Otherwise, calculate
        else
          Some(
            (for(x <- usage; y <- rate) yield tarFuncs.annualCostForEnergyType(x, y, standingCharge))
              .getOrElse(0)) // Set to zero if no usage
      }

      val pCost = energyCost(powerUsage, t.rates.power).map(taxFuncs.toGrossPrice(_, vatRate))
      val gCost = energyCost(gasUsage, t.rates.gas).map(taxFuncs.toGrossPrice(_, vatRate))

      (for(x <- pCost; y <- gCost) yield x + y)
        .map((t.tariff, _))
    }

    tariffs
      .flatMap(calc)
      .toSeq // An ordered collection is not required (Iterable), but we do return one (Seq).
      .sortBy(x => x._2)
  }
}
