package controllers

import play.api.mvc._
import models.{ AirportAndRunways, Query => DAO1 }
import models.{ Report => DAO2 }
import play.api.data._
import play.api.data.Forms._

object QueryWithReport extends Controller {

  /**
   * A method that runs an action to display query results.
   */
  def query(input: String) = Action {
    val getCountry = DAO1.userInput(input)
    val airportWithRunways = DAO1.getAirportsWithRunways(getCountry)
    Ok(views.html.main(airportWithRunways))
  }

  /**
   * A method that runs an action to display report results.
   */
  def report = Action {
    val topTenCountries = DAO2.topTenAirportCountries
    val bottomTenCountries = DAO2.bottomTenAirportsCountries
    val mostCommonRunWays = DAO2.mostCommonRunways
    val runwaysSurfacePerCountry = DAO2.runwaysSurfacePerCountry
    Ok(views.html.report(topTenCountries, bottomTenCountries, mostCommonRunWays, runwaysSurfacePerCountry))
  }
}