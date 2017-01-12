package controllers._2_portfolio

import javax.inject._

import controllers.LoginSession
import models.{User, Project, Portfolio}
import utils.CommonUtil
import play.api.mvc._

/**
  * Created by Hyeonmin on 2016-11-01.
  */
@Singleton
class PortfolioCtrl @Inject()(user: User, projectMdl: Project, portfolioMdl: Portfolio, commonUtil: CommonUtil, loginSession: LoginSession) extends Controller {

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

  def portfolio_list() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val category = request.body.asFormUrlEncoded.get("category").head
    val board_page = request.body.asFormUrlEncoded.get("page").head.toInt
    val hashtag = request.body.asFormUrlEncoded.get("hashtag").head
    val yongdo = request.body.asFormUrlEncoded.get("yongdo").head
    val year = request.body.asFormUrlEncoded.get("year").head
    val gujo = request.body.asFormUrlEncoded.get("gujo").head
    val gyumo = request.body.asFormUrlEncoded.get("gyumo").head
    val gyumoMin = gyumo.split("~")(0)
    val gyumoMax = gyumo.split("~")(1)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 계약 스토리"
    page_data += "login" -> ""
    page_data += "category" -> category
    page_data += "page" -> "portfolio"

    var portfolios = List[Map[String, Any]]()
    var totalCount = 0

    val listAndCount = portfolioMdl.getPortfolios(category, hashtag.replace("@", ""), board_page, yongdo.replace("@", ""), year.replace("@", ""), gujo.replace("@", ""), gyumoMin, gyumoMax)

    portfolios = listAndCount._1
    totalCount = listAndCount._2(0)(".total").toString.toInt
    val count = totalCount / portfolioMdl.pageSize + (if (totalCount % portfolioMdl.pageSize == 0) 0 else 1)

    Ok(views.html._2_portfolio_list(page_data, user_data, commonUtil, portfolios, count, board_page))
  }


}
