package commands

import models.{FuelType, Gas, Power, Tariff}

/**
  * Created by Matt on 20/05/2017.
  */
class UsageCalculator {
  val taxFuncs = new formulae.Tax
  val tarFuncs = new formulae.Tariffs

  /** Returns the annual consumption.
    * If the tariff does not have a rate for the given fuel type, will return None.
    */
  def usage(tariff: Tariff, fuel: FuelType, spend: BigDecimal, vatRate: BigDecimal): Option[BigDecimal] = {
    def getRate(fuelType: FuelType, tariff: Tariff): Option[BigDecimal] = fuelType match {
      case Power => tariff.rates.power
      case Gas => tariff.rates.gas
    }

    def calcAnnualUse(rate: BigDecimal, standingCharge: BigDecimal, spend: BigDecimal) = {
      val preTaxSpend = spend / (1 + (vatRate / 100))
      val annualPreTaxSpend = preTaxSpend * 12
      val annualPreTaxStanding = tarFuncs.annualStandingCharge(standingCharge)
      val annualVariableSpend = annualPreTaxSpend - annualPreTaxStanding

      annualVariableSpend / rate
    }

    val rate = getRate(fuel, tariff)
    rate.map(calcAnnualUse(_, tariff.standingCharge, spend))
  }
}
