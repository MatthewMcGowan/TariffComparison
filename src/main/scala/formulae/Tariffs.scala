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
