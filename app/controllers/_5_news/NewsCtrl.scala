package controllers._5_news

import javax.inject._

import controllers.LoginSession
import models.{News, User}
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2016-12-09.
  */
class NewsCtrl@Inject()(user: User, loginSession: LoginSession,
                        newsMdl: News, commonUtil: CommonUtil
                       ) extends Controller {

  def news() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: NEWS"
    page_data += "login" -> ""
    page_data += "category" -> "news"
    page_data += "page" -> "news"

    Ok(views.html._5_news_03_news(page_data, user_data))
  }

  def news_list() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val board_page = request.body.asFormUrlEncoded.get("page").head.toInt

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 계약 스토리"
    page_data += "login" -> ""
    page_data += "category" -> "news"
    page_data += "page" -> "news"

    var newses = List[Map[String, Any]]()
    var totalCount = 0
    newses = newsMdl.getNewses("", board_page)._1
    totalCount = newsMdl.getNewses("", board_page)._2(0)(".total").toString.toInt
    val count = totalCount / newsMdl.pageSize + (if (totalCount % newsMdl.pageSize == 0) 0 else 1)

    Ok(views.html._5_news_03_news_list(page_data, user_data, commonUtil, newses, count, board_page))
  }

  def news_view(idx: String) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 계약 스토리"
    page_data += "login" -> ""
    page_data += "category" -> "news"
    page_data += "page" -> "news"

    var news = Map[String, Any]()
    news = newsMdl.getANews(idx)

    Ok(views.html._5_news_03_news_view(page_data, user_data, commonUtil, news))
  }

}
