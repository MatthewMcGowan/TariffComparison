package commands

import org.scalatest.FunSpec

/**
  * Created by Matt on 21/05/2017.
  */
class CostCalculatorSpec extends FunSpec {
  describe("A Cost Calculator") {
    val calc = new CostCalculator

    describe("given a single tariff") {
      describe("with both gas and power rates") {
        describe("with both gas and power usage") {
          it("correctly calculates the cost for both power and gas.") {
            ???
          }
        }

        describe("and no power usage") {
          it("correctly calculates the cost for gas only.") {
            ???
          }
        }

        describe("and no gas usage") {
          it("correctly calculates the cost for power only.") {
            ???
          }
        }
      }

      describe("with only a gas rate") {
        describe("with both gas and power usage") {
          it("correctly calculates the cost for gas only.") {
            ???
          }
        }

        describe("and no power usage") {
          it("correctly calculates the cost for gas only.") {
            ???
          }
        }

        describe("and no gas usage") {
          it("returns an empty sequence.") {
            ???
          }
        }
      }

      describe("with only a power rate") {
        describe("with both gas and power usage") {
          it("correctly calculates the cost for power only.") {
            ???
          }
        }

        describe("and no power usage") {
          it("returns an empty sequence.") {
            ???
          }
        }

        describe("and no gas usage") {
          it("correctly calculates the cost for power only.") {
            ???
          }
        }
      }
    }

    describe("given multiple tariffs") {
      it("returns costs sorted with cheapest first") {
        ???
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