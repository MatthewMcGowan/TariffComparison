package commands

import models.{Gas, Power, Rates, Tariff}
import org.scalatest.FunSpec

/**
  * Created by Matt on 21/05/2017.
  */
class UsageCalculatorSpec extends FunSpec {
  describe("A Usage Calculator") {
    val calc = new UsageCalculator

    describe("given fuel type is gas") {
      describe("and a tariff that contains a rate for gas") {
        it("correctly calculates the annual usage") {
          val t = Tariff("t0", Rates(Some(1), Some(2)), 5)

          val result = calc.usage(t, Gas, 110, 10).get

          assert(result == 570)
        }
      }

      describe("with a tariff that does not contain a rate for gas") {
        it("returns None") {
          val t = Tariff("t0", Rates(Some(1), None), 5)

          val result = calc.usage(t, Gas, 110, 10)

          assert(result.isEmpty)
        }
      }
    }

    describe("given fuel type is power") {
      describe("and a tariff that contains a rate for gas") {
        it("correctly calculates the annual usage") {
          val t = Tariff("t0", Rates(Some(1), Some(2)), 5)

          val result = calc.usage(t, Power, 110, 10).get

          assert(result == 1140)
        }
      }

      describe("with a tariff that does not contain a rate for power") {
        it("returns None") {
          val t = Tariff("t0", Rates(None, Some(2)), 5)

          val result = calc.usage(t, Power, 110, 10)

          assert(result.isEmpty)
        }
      }
    }
  }
}