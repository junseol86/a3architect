package controllers._5_news

import javax.inject._

import controllers.LoginSession
import models.{Promotion, User}
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2016-12-09.
  */
class PromotionCtrl@Inject()(user: User, loginSession: LoginSession,
                             promotionMdl: Promotion, commonUtil: CommonUtil
                       ) extends Controller {

  def promotion() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: NEWS"
    page_data += "login" -> ""
    page_data += "category" -> "news"
    page_data += "page" -> "promotion"

    Ok(views.html._5_news_01_promotion(page_data, user_data))
  }

  def promotion_list() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val category = request.body.asFormUrlEncoded.get("category").head
    val board_page = request.body.asFormUrlEncoded.get("page").head.toInt

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 계약 스토리"
    page_data += "login" -> ""
    page_data += "category" -> "news"
    page_data += "page" -> "promotion"

    var promotions = List[Map[String, Any]]()
    var totalCount = 0
    promotions = promotionMdl.getPromotions(category.replace("@", ""), board_page)._1
    totalCount = promotionMdl.getPromotions(category.replace("@", ""), board_page)._2(0)(".total").toString.toInt
    val count = totalCount / promotionMdl.pageSize + (if (totalCount % promotionMdl.pageSize == 0) 0 else 1)

    Ok(views.html._5_news_01_promotion_list(page_data, user_data, commonUtil, promotions, count, board_page))
  }

//  def promotion_view(idx: String) = Action { request =>
//    var user_data = List[Map[String, Any]]()
//    user_data = loginSession.userData(request)
//
//    var page_data = Map[String, Any]()
//    page_data += "title" -> "A3 :: 계약 스토리"
//    page_data += "login" -> ""
//    page_data += "category" -> "promotion"
//    page_data += "page" -> "promotion"
//
//    var promotion = Map[String, Any]()
//    promotion = promotionMdl.getAPromotion(idx)
//
//    Ok(views.html._5_news_01_promotion_view(page_data, user_data, commonUtil, promotion))
//  }

}
