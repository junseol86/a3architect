package controllers._A_admin

import javax.inject._

import controllers.LoginSession
import models.{Portfolio, Deleter, User}
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2017-01-11.
  */
class AdminPortfolioCtrl @Inject()(user: User, loginSession: LoginSession,
                                   portfolioMdl: Portfolio, deleter: Deleter,
                                   commonUtil: CommonUtil) extends Controller {

  def portfolio_write(pj_idx: String, pf_category: String) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "portfolio"
    page_data += "left_menu" -> "project"
    page_data += "pf_category" -> pf_category
    page_data += "pj_idx" -> pj_idx

    var portfolio:Map[String, Any] = null

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_portfolio_write(page_data, user_data, commonUtil, portfolio))
    else
      Redirect("/")
  }

//  def portfolio_modify(idx:String) = Action { request =>
//    var user_data = List[Map[String, Any]]()
//    user_data = loginSession.userData(request)
//
//    var page_data = Map[String, Any]()
//    page_data += "title" -> "A3 :: 관리자 페이지"
//    page_data += "login" -> ""
//    page_data += "category" -> "admin"
//    page_data += "page" -> "portfolio"
//    page_data += "left_menu" -> "portfolio"
//
//    var portfolio = Map[String, Any]()
//    portfolio = portfolioMdl.getAPortfolio(idx)
//
//    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
//      Ok(views.html._A_admin_portfolio_write(page_data, user_data, commonUtil, portfolio))
//    else
//      Redirect("/")
//  }


}
