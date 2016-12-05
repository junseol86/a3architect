package controllers._A_admin

import javax.inject._

import controllers.LoginSession
import models.{Consulting, User}
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2016-11-28.
  */
class AdminCtrl @Inject()(user: User, loginSession: LoginSession, consulting: Consulting, commonUtil: CommonUtil) extends Controller  {

  def myinfo = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "myinfo"
    page_data += "left_menu" -> "myinfo"

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_myinfo(page_data, user_data))
    else
      Redirect("/")
  }

  def contract_story = Action { request =>
    Redirect("/admin/contract_story_list/1/1")
  }

  def contract_story_list(category: Int, page: Int) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "contract_story"
    page_data += "left_menu" -> "contract_story"

    var list_data = Map[String, Any]()
    list_data += "category" -> category.toString
    list_data += "page" -> page.toString

    var stories = List[Map[String, Any]]()
    stories = consulting.getContractStories(category, "", page)

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_contract_stories(page_data, user_data, list_data, stories))
    else
      Redirect("/")
  }

  def contract_story_write = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "contract_story"
    page_data += "left_menu" -> "contract_story"

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_contract_stories_write(page_data, user_data))
    else
      Redirect("/")
  }

}
