package controllers._2_portfolio

import javax.inject._

import controllers.LoginSession
import models.{User, Project, Portfolio}
import play.api.mvc._

/**
  * Created by Hyeonmin on 2016-11-01.
  */
@Singleton
class PortfolioCtrl @Inject()(user: User, projectMdl: Project, portfolioMdl: Portfolio, loginSession: LoginSession) extends Controller {

  def portfolio(category:String) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 건축시공"
    page_data += "login" -> ""
    page_data += "category" -> "portfolio"
    page_data += "page" -> "construction"
    page_data += "category" -> category

    var options = Map[String, List[Map[String, Any]]]()
    options = projectMdl.getOptions

    var years = List[Map[String, Any]]()
    years = projectMdl.getYears

    Ok(views.html._2_portfolio(page_data, user_data, options, years))
  }

}
