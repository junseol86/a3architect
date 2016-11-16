package controllers._2_portfolio

import javax.inject._

import controllers.LoginSession
import models.User
import play.api.mvc._

/**
  * Created by Hyeonmin on 2016-11-01.
  */
@Singleton
class PortfolioCtrl @Inject()(user: User, loginSession: LoginSession) extends Controller {

  def construction = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3그룹은?"
    page_data += "login" -> ""
    page_data += "category" -> "portfolio"
    page_data += "page" -> "construction"

    Ok(views.html._2_portfolio_01_construction(page_data, user_data))
  }

}
