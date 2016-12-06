package controllers._A_admin

import javax.inject._

import controllers.LoginSession
import models.{Consulting, ContractStory, User}
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2016-11-28.
  */
class AdminCtrl @Inject()(user: User, loginSession: LoginSession,
                          consulting: Consulting, contractStory: ContractStory,
                          commonUtil: CommonUtil) extends Controller  {

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
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "contract_story"
    page_data += "left_menu" -> "contract_story"

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_contract_stories(page_data, user_data))
    else
      Redirect("/")
  }

  def contract_story_list(category: Int, board_page: Int) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "contract_story"
    page_data += "left_menu" -> "contract_story"

    var stories = List[Map[String, Any]]()
    var totalCount = 0
    stories = contractStory.getContractStories(category, "", board_page)._1
    totalCount = contractStory.getContractStories(category, "", board_page)._2(0)(".total").toString.toInt
    val count = totalCount / contractStory.pageSize + (if (totalCount % contractStory.pageSize == 0) 0 else 1)

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_contract_stories_list(page_data, user_data, stories, count, board_page))
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

  def contract_story_submit = Action { request =>

    val fp = new commonUtil.FromPost(request)

    var client_id = ""
    request.session.get("user_id").map { id =>
      client_id = id
    }

    val insert = contractStory.contractStoryWrite(fp.get("category"), fp.get("title"), fp.get("ir1"), fp.get("modified"), fp.get("modified"))
    val insertResult = insert match {
      case None => ""
      case Some(i: Long) => i.toString
    }

    Ok(insert match {
      case None => views.html.alert_and_move("신청서가 접수되지 않았습니다.  다시 시도해 주십시오.", "/admin/contract_story_write")
      case Some(i: Long) => views.html.alert_and_move("신청서가 접수되었습니다.  곧 연락드리겠습니다.", "/admin/contract_story_write")
    })


  }

}
