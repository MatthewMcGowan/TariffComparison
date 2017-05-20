import models.Tariff

import scala.io.Source.fromResource

/**
  * Created by Matt on 20/05/2017.
  */
object TariffComparisonConsole extends App {
  val tariffParser = new TariffParser

  val tariffJson = fromResource("prices.json").getLines().mkString("\n")
  val tariffs = tariffParser.parse(tariffJson)

  tariffs match {
    case Some(x) => processUserCommands(x)
    case None => println("Tariff data in prices.json cannot be parsed. Exiting.")
  }

  def processUserCommands(tariffs: Seq[Tariff]) = {
    println(tariffs)
  }
}
