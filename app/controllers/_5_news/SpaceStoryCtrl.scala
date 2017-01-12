package controllers._5_news

import javax.inject._

import controllers.LoginSession
import models.{SpaceStory, User}
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2016-12-09.
  */
class SpaceStoryCtrl@Inject()(user: User, loginSession: LoginSession,
                              SpaceStoryMdl: SpaceStory, commonUtil: CommonUtil
                       ) extends Controller {

  def space_story() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: NEWS"
    page_data += "login" -> ""
    page_data += "category" -> "news"
    page_data += "page" -> "space_story"

    Ok(views.html._5_news_02_space_story(page_data, user_data))
  }

  def space_story_list() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val category = request.body.asFormUrlEncoded.get("category").head
    val board_page = request.body.asFormUrlEncoded.get("page").head.toInt

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 계약 스토리"
    page_data += "login" -> ""
    page_data += "category" -> "news"
    page_data += "page" -> "space_story"

    var spacestories = List[Map[String, Any]]()
    var totalCount = 0

    val listAndCount = SpaceStoryMdl.getSpaceStories(category.replace("@", ""), board_page)

    spacestories = listAndCount._1
    totalCount = listAndCount._2(0)(".total").toString.toInt
    val count = totalCount / SpaceStoryMdl.pageSize + (if (totalCount % SpaceStoryMdl.pageSize == 0) 0 else 1)

    Ok(views.html._5_news_02_space_story_list(page_data, user_data, commonUtil, spacestories, count, board_page))
  }

  def space_story_view(idx: String) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 계약 스토리"
    page_data += "login" -> ""
    page_data += "category" -> "news"
    page_data += "page" -> "space_story"

    var space_story = Map[String, Any]()
    space_story = SpaceStoryMdl.getASpaceStory(idx)

    Ok(views.html._5_news_02_space_story_view(page_data, user_data, commonUtil, space_story))
  }

}
