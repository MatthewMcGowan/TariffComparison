/**
  * Created by Matt on 21/05/2017.
  */
package object TariffCustomTypes {
  type kWh = Int
  type Percent = BigDecimal // Used for VAT rate.
  // Should 5% VAT be "5" or "0.05"? By aliasing type to "Percent" it becomes clear that it is "5: Percent".
}
