package models

import scala.io.Source
import scala.util.Try
import java.io.File
import models.Report._
import models.Query._

/**
 * @author Vivek
 */

/**
 * Creating a container for Airports with country, airport name, identifier and runways.
 */
case class AirportAndRunways(country: String, airportName: String, airportIdentifier: String, runways: List[String] = List[String]())

object Airports {

  /**
   * Loading input files
   */
  val airportsInputFile: String = "./public/airports.csv"
  val countriesInputFile: String = "./public/countries.csv"
  val runwaysInputFile: String = "./public/runways.csv"

  /**
   * Loading input files into memory and removing double quotes.
   */
  val airportsData = Source.fromFile(new File(airportsInputFile), "ISO-8859-1").getLines.drop(1).toList map { _.replaceAll("\"", "") }
  val countriesData: List[String] = Source.fromFile(new File(countriesInputFile), "ISO-8859-1").getLines.drop(1).toList map { _.replaceAll("\"", "") }
  val runwaysData: List[String] = Source.fromFile(new File(runwaysInputFile), "ISO-8859-1").getLines.drop(1).toList map { _.replaceAll("\"", "") }

  /**
   * Creating a lookup map for countries.
   */
  val CodetoCountryMap: Map[String, String] = countriesData.map { x => (x.split(",")(1), x.split(",")(2)) }.toMap
  val CountryToCodeMap: Map[String, String] = CodetoCountryMap.map { case (code, country) => (country, code) }

  /**
   * A method that gets country code from country name
   */
  def codeFromCountry(countryName: String): String = Try { CountryToCodeMap.get(countryName).get }.getOrElse("")

  /**
   * A method that gets country name from country code
   */
  def countryFromCode(countryCode: String): String = Try { CodetoCountryMap.get(countryCode).get }.getOrElse("")

}