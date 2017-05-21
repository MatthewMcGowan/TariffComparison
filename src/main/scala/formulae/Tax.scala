package formulae

import TariffCustomTypes.Percent

/**
  * Created by Matt on 20/05/2017.
  */
class Tax {
  /** Calculates the Gross Price from the Net Price and VAT rate */
  def toGrossPrice(netPrice: BigDecimal, vatRate: Percent): BigDecimal = (1 + (vatRate / 100)) * netPrice
}
