package formulae

/**
  * Created by Matt on 20/05/2017.
  */
class Tax {
  /** Calculates the Gross Price from the Net Price and VAT rate */
  def grossPrice(netPrice: BigDecimal, vatRate: BigDecimal): BigDecimal = (1 + vatRate) * netPrice
}
