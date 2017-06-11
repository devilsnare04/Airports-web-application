package models

import models.Airports._
import scala.util.Try

/**
 * @author Vivek
 */
object Report {

  /**
   * A List with Tuple2 of country name and the count of airports.
   */
  val groupByMapAirportsCount: List[(String, Int)] = airportsData.map(_.split(",")(8)).groupBy(identity).mapValues { _.size }.toList

  /**
   * Applying ascending and descending sort to List of country names with airport count.
   */
  val reverseSortedCountries: List[(String, Int)] = groupByMapAirportsCount.sortWith(-_._2 < -_._2)
  val sortedCountries: List[(String, Int)] = groupByMapAirportsCount.sortWith(_._2 < _._2)

  /**
   * Attaining top and bottom 10 airport countries.
   */
  val topTenAirportCountries: List[(String, Int)] = reverseSortedCountries.take(10).map { case (code, count) => (countryFromCode(code), count) }
  val bottomTenAirportsCountries: List[(String, Int)] = sortedCountries.take(10).map { case (code, count) => (countryFromCode(code), count) }

  /**
   * A map of airport id with the surface types.
   */
  val mapSurfaceType: Map[String, String] = runwaysData.map { x => (x.split(",")(2), x.split(",")(5)) }.toMap

  /**
   * A list with tuple2 containing country with runway's surface type utilizing mapRunwaysType.
   */
  val listCountryType: List[(String, String)] = airportsData.map { x =>
    (countryFromCode(x.split(",")(8)),
      Try { mapSurfaceType.get(x.split(",")(1)).get }.getOrElse(""))
  }

  /**
   * grouping the above list structure to obtain a country with its set of runways.
   * Also filtering out countries for which surface information is not available.
   */
  val runwaysSurfacePerCountry: Map[String, Set[String]] = listCountryType groupBy (_._1) mapValues
    { x => x.filterNot { _._2 == "" }.map(_._2).toSet }

  /**
   * Top 10 most common runway identifications using reverse sort.
   */
  val mostCommonRunways: List[String] = runwaysData.map(x => Try { x.split(",")(8) }.getOrElse("")).groupBy(identity)
    .mapValues { _.size }.toList.sortWith(-_._2 < -_._2).take(10).map { case (id, count) => id }

}