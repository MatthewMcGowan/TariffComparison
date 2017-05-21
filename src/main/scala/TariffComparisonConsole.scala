import commands.{CostCalculator, UsageCalculator}
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
  val costCalc = new CostCalculator
  val usageCalc = new UsageCalculator
  val vatRate = 5

  val tariffJson = fromResource("prices.json").getLines().mkString("\n")
  val tariffs = tariffParser.parse(tariffJson)

  tariffs match {
    case Some(Nil) => println("No tariff data in provided prices.json. Exiting.")
    case None => println("Tariff data in prices.json cannot be parsed. Exiting.")
    case Some(x) => {
      val tNames = x.map(_.tariff)
      if(tNames.distinct.size != tNames.size) println("Tariff data contains duplicate tariff name. Exiting.")
      else {
        printIntro()
        processUserCommands(x)
      }
    }
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
      println("Arguments for cost command must be valid integer values")
    else {
      val costs = costCalc.costs(tariffs, p, g, vatRate)
      costs.foreach(c => println(s"${c._1} ${dFormat(c._2)}"))
    }
  }

  def usageReq(input: Array[String], tariffs: Seq[Tariff]): Unit = {
    val tariffName = input(1)
    val fuelType = FuelType.fromString(input(2))
    val targetSpend = tryToDouble(input(3))

    if (targetSpend.isEmpty)
      println("Target spend must be valid number.")
    else if (fuelType.isEmpty)
      println("Invalid fuel type provided.")
    else if (tariffs.count(_.tariff == tariffName) < 1)
      println("No tariff data found for given tariff name.")
    else {
      val t = tariffs.find(_.tariff == tariffName)
      val annualConsumption = usageCalc.usage(t.get, fuelType.get, targetSpend.get, vatRate)
      annualConsumption match {
        case Some(c) => println(dFormat(c))
        case None => println("Tariff does not contain data for this fuel type.")
      }
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
