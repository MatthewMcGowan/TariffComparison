import TariffCustomTypes.Percent
import com.typesafe.config.ConfigFactory
import commands.{CostCalculator, UsageCalculator}
import models.{FuelType, Tariff}

import scala.annotation.tailrec
import scala.io.Source.fromResource
import scala.io.StdIn.readLine
import scala.util.Try

/**
  * Created by Matt on 20/05/2017.
  *
  * Responsible for running the application in the console.
  * Responsible for handling IO with user.
  */
object TariffComparisonConsole extends App {
  val config = ConfigFactory.load()

  val tariffParser = new TariffParser
  val costCalc = new CostCalculator
  val usageCalc = new UsageCalculator
  val vatRate: Percent = config.getDouble("calcuationConstants.vatPercent")

  // Load the JSON, treated like user input
  val tariffJson = fromResource("prices.json").getLines().mkString("\n")
  val tariffs = tariffParser.parse(tariffJson)

  tariffs match {
    case Some(Nil) => println("No tariff data in provided prices.json. Exiting.")
    case None => println("Tariff data in prices.json cannot be parsed. Exiting.")
    case Some(x) => {
      val tNames = x.map(_.tariff)
      if(tNames.distinct.size != tNames.size)
        println("Tariff data contains duplicate tariff name. Exiting.")
      else {
        printIntro()
        processUserCommands(x)
      }
    }
  }

  // With the prices.json loaded, we loop around this method continually
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

  // Handle a "cost" input
  def costReq(input: Array[String], tariffs: Seq[Tariff]): Unit = {
    def toNonZeroUsage(u: Int) = u match {
      case 0 => None
      case _ => Some(u)
    }

    // Get the command arguments, if they're supplied
    val powerUsage = Try(input(1)).toOption.flatMap(tryToInt)
    val gasUsage = Try(input(2)).toOption.flatMap(tryToInt)

    // Ensure arguments are valid
    if (powerUsage.isEmpty || gasUsage.isEmpty)
      println("Arguments for cost command must be present, valid integer values")
    else {
      // If input usage is zero, we take this to mean customer is not supplied with this energy type.
      // We replace sentinel value with None.
      val p = powerUsage.flatMap(toNonZeroUsage)
      val g = gasUsage.flatMap(toNonZeroUsage)

      // Calculate and print
      val costs = costCalc.costs(tariffs, p, g, vatRate)
      costs.foreach(c => println(s"${c._1} ${dFormat(c._2)}"))
    }
  }

  def usageReq(input: Array[String], tariffs: Seq[Tariff]): Unit = {
    val tariffName = Try(input(1)).toOption
    val fuelType = Try(input(2)).toOption.flatMap(FuelType.fromString)
    val targetSpend = Try(input(3)).toOption.flatMap(tryToDouble)

    // Validate all the input...
    if (tariffName.isEmpty || tariffs.count(_.tariff == tariffName.get) < 1)
      println("Tariff name must be provided for a Tariff in prices.json.")
    else if (fuelType.isEmpty)
      println("Fuel type must be present and a valid value.")
    else if (targetSpend.isEmpty)
      println("Target spend must be present and a valid number.")
    else {
      val t = tariffs.find(_.tariff == tariffName.get)

      // Calculate the annual consumption
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
