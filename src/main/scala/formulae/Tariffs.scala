package formulae

import TariffCustomTypes.kWh

/**
  * Created by Matt on 20/05/2017.
  */
class Tariffs {
  /** Calculates the annual sum of the monthly standing charge.
    * Will throw an arithmetic exception, with extremely large values.
    */
  def annualCost(monthlyCost: BigDecimal): BigDecimal = 12 * monthlyCost
  // Seems trivial, but perhaps it could be (x / 30) * 365. Better to be explicitly defined.

  /** Calculates the annual total cost for one energy type. */
  def annualCostForEnergyType(annualUsage: kWh, rate: BigDecimal, annualStandingCharge: BigDecimal): BigDecimal =
    (annualUsage * rate) + annualStandingCharge
}

/* Both this and the Tax formulae class are intended to start separating out some  of the basic calculations into a
 * library.
 * Currently they are not independently unit tested, because in this situation what improvement would the tests actually
 * be driving? Asserting that multiplying a number by 12 gives you 12 times that number gives us little, but were this
 * to actually become a library consumed in multiple places trivial unit tests start to become more important.
 * Within this application, however, they are covered in tests by their consumers: CostCalculator and UsageCalculator.
 */