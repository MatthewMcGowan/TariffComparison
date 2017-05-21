import models.{Rates, Tariff}
import org.scalatest.FunSpec

/**
  * Created by Matt on 20/05/2017.
  */
//TODO: Cover optional values in the JSON explicitly
class TariffParserSpec extends FunSpec {
  describe("A TariffParser") {
    val tariffParser = new TariffParser

    describe("given valid JSON") {
      describe("with a tariff present") {
        it("should return the tariff") {
          val t = tariffParser.parse(singleTariffJson).get.head
          assert(t == tariffs.head)
        }
      }
      describe("with multiple tariffs present") {
        it("should return correct number of tariffs") {
          val ts = tariffParser.parse(mutlipleTariffJson).get
          assert(ts == tariffs)
        }

        it("should return tariffs correctly deserialised") {
          val ts = tariffParser.parse(mutlipleTariffJson).get
          ts.forall(tariffs.contains(_))
        }

        // Although the sequential implementation results in input order being preserved, this is not in spec.
        // Seq in this case is still chosen over Iterable though for API.
      }
      describe("with no tariffs present") {
        it("should return an empty List") {
          val t = tariffParser.parse(emptyJson).get
          assert(t.isEmpty)
        }
      }
    }
    describe("given invalid JSON") {
      it("should return None") {
        val t = tariffParser.parse(invalidJson)
        assert(t.isEmpty)
      }
    }
  }

  private val tariffs = Seq(
    Tariff("better-energy", Rates(Some(0.1367), Some(0.0288)), 8.33),
    Tariff("2yr-fixed", Rates(Some(0.1397), Some(0.0296)), 8.75),
    Tariff("greener-energy", Rates(Some(0.1544), None), 8.33),
    Tariff("simpler-energy", Rates(Some(0.1396), Some(0.0328)), 8.75)
  )
  private val singleTariffJson =
    """[{"tariff": "better-energy", "rates": {"power":  0.1367, "gas": 0.0288}, "standing_charge": 8.33}]"""
  private val mutlipleTariffJson =
    """[
      |  {"tariff": "better-energy", "rates": {"power":  0.1367, "gas": 0.0288}, "standing_charge": 8.33},
      |  {"tariff": "2yr-fixed", "rates": {"power": 0.1397, "gas": 0.0296}, "standing_charge": 8.75},
      |  {"tariff": "greener-energy", "rates": {"power":  0.1544}, "standing_charge": 8.33},
      |  {"tariff": "simpler-energy", "rates": {"power":  0.1396, "gas": 0.0328}, "standing_charge": 8.75}
      |]""".stripMargin
  private val emptyJson =
    """[]"""
  private val invalidJson =
    """tariff: invalid"""
}
