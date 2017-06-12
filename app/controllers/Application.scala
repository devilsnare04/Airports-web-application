package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  /**
   * An action that redirects to QueryWithReport's query method.
   */
  def index = Action {
    Redirect(routes.QueryWithReport.query())
  }

}