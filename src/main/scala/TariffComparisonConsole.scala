import commands.{CostCalc, UsageCalc}
import models.{FuelType, Tariff}

import scala.annotation.tailrec
import scala.io.Source.fromResource
import scala.io.StdIn.readLine
import scala.util.Try

/**
  * Created by Matt on 20/05/2017.
  */
object TariffComparisonConsole extends App {
  val tariffParser = new TariffParser
  val costCalc = new CostCalc
  val usageCalc = new UsageCalc
  val vatRate = 5

  val tariffJson = fromResource("prices.json").getLines().mkString("\n")
  val tariffs = tariffParser.parse(tariffJson)

  tariffs match {
    case Some(x) => processUserCommands(x)
    case None => println("Tariff data in prices.json cannot be parsed. Exiting.")
  }

  @tailrec
  def processUserCommands(tariffs: Seq[Tariff]): Unit = {
    val input = readLine().split(' ')

    input match {
      case Array("cost", _*) => {
        costReq(input, tariffs)
        processUserCommands(tariffs)
      }
      case Array("usage", _*) => {
        usageReq(input, tariffs)
        processUserCommands(tariffs)
      }
      case Array("exit") => {
        Unit
      }
      case _ => {
        println("Unsupported command, please try again.")
        processUserCommands(tariffs)
      }
    }
  }

  def costReq(input: Array[String], tariffs: Seq[Tariff]): Unit = {
    val powerUsage = tryToInt(input(1))
    val gasUsage = tryToInt(input(2))

    def toNonZeroUsage(u: Int) = u match {
      case 0 => None
      case _ => Some(u)
    }

    val p = toNonZeroUsage(powerUsage.get)
    val g = toNonZeroUsage(gasUsage.get)

    if (powerUsage.isEmpty || gasUsage.isEmpty)
      println("Arguments for cost command must be integer values")
    else {
      val costs = costCalc.costs(tariffs, p, g, vatRate)
      costs.foreach(c => println(s"${c._1} ${dFormat(c._2)}"))
    }
  }

  def usageReq(input: Array[String], tariffs: Seq[Tariff]): Unit = {
    val tariff = input(1)
    val fuelType = FuelType.fromString(input(2))
    val targetSpend = tryToDouble(input(3))

    if (targetSpend.isEmpty)
      println("Target spend must be valid number.")
    else if (fuelType.isEmpty)
      println("Invalid fuel type provided.")
    else{
      val annualConsumption = usageCalc.usage(tariffs, tariff, fuelType.get, targetSpend.get, vatRate)
      println(annualConsumption)
    }
  }

  private def printIntro(): Unit = {
    println("""Welcome to the console tariff comparison tool.
              |Supported commands:
              |- cost <POWER_USAGE> <GAS_USAGE>
              |- usage <TARIFF_NAME> <FUEL_TYPE> <TARGET_MONTHLY_SPEND>
              |- exit
              |Enter command...""".stripMargin)
  }

  private def tryToInt(s: String): Option[Int] = Try(s.toInt).toOption
  private def tryToDouble(s: String): Option[Double] = Try(s.toDouble).toOption

  private def dFormat(d: BigDecimal): String = "%.2f".format(d)
}
