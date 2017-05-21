package commands

import models.{Rates, Tariff}
import org.scalatest.FunSpec

/**
  * Created by Matt on 21/05/2017.
  */
class CostCalculatorSpec extends FunSpec {
  // These test cases are the result of manual calculation.
  // In a work scenario, in stead of being single examples, each would ideally these would be a larger set of examples
  // agreed with Product Owner or BAs.

  describe("A Cost Calculator") {
    val calc = new CostCalculator

    describe("given a single tariff") {
      // If a given usage is present, we are only interested in Tariffs which provide that energy type.
      // If a required rate is missing, this Tariff is not applicable.
      // However, a customer may take a tariff but not be supplied for, and have no usage for, one energy type.

      describe("with both gas and power rates") {
        describe("with both gas and power usage") {
          it("correctly calculates the cost for both power and gas.") {
            val tariff = Tariff("t0", Rates(Some(1), Some(2)), 10)
            val result = calc.costs(tariff :: Nil, Some(10), Some(20), 10).head._2
            assert(result == 319)
          }
        }

        describe("and no power usage") {
          it("correctly calculates the cost for gas only.") {
            val tariff = Tariff("t0", Rates(Some(1), Some(2)), 10)
            val result = calc.costs(tariff :: Nil, None, Some(20), 10).head._2
            assert(result == 176)
          }
        }

        describe("and no gas usage") {
          it("correctly calculates the cost for power only.") {
            val tariff = Tariff("t0", Rates(Some(1), Some(2)), 10)
            val result = calc.costs(tariff :: Nil, Some(10), None, 10).head._2
            assert(result == 143)
          }
        }
      }

      describe("with only a gas rate") {
        describe("with both gas and power usage") {
          it("returns an empty sequence.") {
            val tariff = Tariff("t0", Rates(None, Some(2)), 10)
            val result = calc.costs(tariff :: Nil, Some(10), Some(20), 10)
            assert(result.isEmpty)
          }
        }

        describe("and no power usage") {
          it("correctly calculates the cost for gas only.") {
            val tariff = Tariff("t0", Rates(None, Some(2)), 10)
            val result = calc.costs(tariff :: Nil, None, Some(20), 10).head._2
            assert(result == 176)
          }
        }

        describe("and no gas usage") {
          it("returns an empty sequence.") {
            val tariff = Tariff("t0", Rates(None, Some(2)), 10)
            val result = calc.costs(tariff :: Nil, Some(10), None, 10)
            assert(result.isEmpty)
          }
        }
      }

      describe("with only a power rate") {
        describe("with both gas and power usage") {
          it("correctly calculates the cost for power only.") {
            val tariff = Tariff("t0", Rates(Some(1), None), 10)
            val result = calc.costs(tariff :: Nil, Some(10), Some(20), 10)
            assert(result.isEmpty)
          }
        }

        describe("and no power usage") {
          it("returns an empty sequence.") {
            val tariff = Tariff("t0", Rates(Some(1), None), 10)
            val result = calc.costs(tariff :: Nil, None, Some(20), 10)
            assert(result.isEmpty)
          }
        }

        describe("and no gas usage") {
          it("returns an empty sequence.") {
            val tariff = Tariff("t0", Rates(Some(1), None), 10)
            val result = calc.costs(tariff :: Nil, Some(10), None, 10).head._2
            assert(result == 143)
          }
        }
      }
    }

    describe("given multiple tariffs") {
      it("returns costs sorted with cheapest first") {
        val tariffs = Seq(
          Tariff("t0", Rates(Some(30), Some(30)), 30),
          Tariff("t1", Rates(Some(10), Some(10)), 10),
          Tariff("t2", Rates(Some(20), Some(20)), 20)
        )

        val result = calc.costs(tariffs, Some(1), Some(1), 1).map(x => x._1)
        assert(result == Seq("t1", "t2", "t0"))
      }
    }

    describe("given an empty Sequence of tariffs") {
      it("returns an empty sequence.") {
        val result = calc.costs(Iterable(), Some(1), Some(1), 1)
        assert(result.isEmpty)
      }
    }
  }
}