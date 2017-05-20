package formulae

/**
  * Created by Matt on 20/05/2017.
  */
class Tariffs {
  // Seems trivial, but perhaps it could be (x / 30) * 365. Better to be explicitly defined.

  /** Calculates the annual sum of the monthly standing charge.
    * This could throw an arithmetic exception, with extremely large values, but we currently choose to assume these
    * will not occur.
    */
  def annualStandingCharge(monthlyStandingCharge: BigDecimal): BigDecimal = 12 * monthlyStandingCharge
}
