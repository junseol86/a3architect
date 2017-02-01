package controllers._0_dashboard

import javax.inject._

import controllers.LoginSession
import models.{Portfolio, Sites, Promotion, SpaceStory, News, ContractStory}
import play.api.mvc._
import models.User
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2016-11-01.
  */
@Singleton
class DashboardCtrl @Inject()(
                               user: User, loginSession: LoginSession, commonUtil: CommonUtil,
                               promotionMdl: Promotion, spaceStoryMdl: SpaceStory,
                               portfolioMdl: Portfolio, sitesMdl: Sites,
                               newsMdl: News, contractStoryMdl: ContractStory)
  extends Controller {

  def index = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 그룹"
    page_data += "category" -> "index"
    page_data += "page" -> "dashboard"

    var promotion = List[Map[String, Any]]()
    promotion = promotionMdl.getRecents()
    var spaceStory = List[Map[String, Any]]()
    spaceStory = spaceStoryMdl.getRecents()

    var topPortfolios = List[Map[String, Any]]()
    topPortfolios = portfolioMdl.getTopPortfolios()

    var newses = List[Map[String, Any]]()
    newses = newsMdl.getDashboardNewses()

    var contractStories = List[Map[String, Any]]()
    contractStories = contractStoryMdl.getDashboardContractStories()

    Ok(views.html._0_dashboard(page_data, user_data, commonUtil, promotion, spaceStory, topPortfolios, newses, contractStories))
  }

  def portfolioList = Action {request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 그룹"
    page_data += "category" -> "index"
    page_data += "page" -> "dashboard"

    var portfolios = List[Map[String, Any]]()
    portfolios = portfolioMdl.getDashboardPortfolios(fp.get("category"))

    Ok(views.html._0_dashboard_portfolio_list(page_data, user_data, commonUtil, portfolios))
  }

  def sitesList = Action {request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 그룹"
    page_data += "category" -> "index"
    page_data += "page" -> "dashboard"

    var siteses = List[Map[String, Any]]()
    siteses = sitesMdl.getDashboardSiteses(fp.get("category"))

    Ok(views.html._0_dashboard_sites_list(page_data, user_data, commonUtil, siteses))
  }
}
