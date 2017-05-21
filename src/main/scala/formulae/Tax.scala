package formulae

import TariffCustomTypes.Percent

/**
  * Created by Matt on 20/05/2017.
  */
class Tax {
  /** Calculates the Gross Price from the Net Price and VAT rate */
  def toGrossPrice(netPrice: BigDecimal, vatRate: Percent): BigDecimal = (1 + (vatRate / 100)) * netPrice

  /** Calculates the Net Price from the Gross Price and VAT rate */
  def toNetPrice(grossPrice: BigDecimal, vatRate: Percent): BigDecimal = grossPrice / (1 + (vatRate / 100))
}
