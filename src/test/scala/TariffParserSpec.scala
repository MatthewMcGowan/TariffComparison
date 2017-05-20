import org.scalatest.FunSpec

/**
  * Created by Matt on 20/05/2017.
  */
class TariffParserSpec extends FunSpec {
  describe("A TariffParser") {
    describe("given valid JSON") {
      describe("with a tariff present") {
        it("should return the tariff") {
          ???
        }
      }
      describe("with multiple tariffs present") {
        it("should return all tariffs") {
          ???
        }
      }
      describe("with no tariffs present") {
        it("should return None") {
          ???
        }
      }
    }
    describe("given invalid JSON") {
      it("should return None") {
        ???
      }
    }
  }
}
