package models

import models.Airports._
import scala.util.Try

/**
 * @author Vivek
 */
object Query {

  /**
   * A method to interpret the users input and return country code if country entered.
   */
  def userInput(inputCodeMessage: String): String = inputCodeMessage match {
    case "" => "Please enter country name or code"
    case x  => deriveCountryCode(x.toUpperCase())
  }

  /**
   * A method to derive the country from supplied country code or country name or partial country name using fuzzy matching logic.
   * As of now just a unique set of fuzzy names are implemented.
   */
  def deriveCountryCode(passedInput: String): String = passedInput.length match {
    case 2 => passedInput
    case _ => codeFromCountry(countryFromPartialName(passedInput))
  }

  /**
   * A fuzzy matching logic method to interpret/find the name from partial or full supplied names.
   */
  def countryFromPartialName(passedInput: String, matchLogic: String = "FULL"): String = matchLogic match {
    case "FULL" => Try {
      countriesData.find { _.split(",")(2).toLowerCase() == passedInput.toLowerCase() }.
        map { _.split(",")(2) }.toList.head
    }.getOrElse(countryFromPartialName(passedInput, "PARTIAL"))
    case "PARTIAL" => Try {
      countriesData.find { _.split(",")(2).substring(0, passedInput.length).toLowerCase() == passedInput.toLowerCase() }.
        map { _.split(",")(2) }.toList.head
    }.getOrElse(countryFromPartialName(passedInput, "UNMATCHED"))
    case _ => ""
  }

  /**
   *  A method to traverse, merge and combine all required information which has to be output together.
   */
  def getAirportsWithRunways(inputCountryCode: String): List[AirportAndRunways] = {

    val countryFromCountryCode: String = countryFromCode(inputCountryCode)

    val airportList: List[AirportAndRunways] = airportsData.filter { _.split(",")(8) == inputCountryCode }.
      map { x => AirportAndRunways(countryFromCountryCode, x.split(",")(3), x.split(",")(1)) }.toList

    val listRequiredAirports: List[String] = airportList.map { x => x.airportIdentifier }
    val listRequiredRunways: List[String] = runwaysData.filter { x => listRequiredAirports.contains(x.split(",")(2)) }.toList

    airportList.map { x =>

      val runwaysList: List[String] = listRequiredRunways.filter { _.split(",")(2) == x.airportIdentifier }.toList
      x.copy(runways = runwaysList)
    }

  }

}