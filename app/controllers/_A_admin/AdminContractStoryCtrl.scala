package controllers._A_admin

import javax.inject._

import controllers.LoginSession
import models.{Consulting, ContractStory, User}
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2016-11-28.
  */
class AdminContractStoryCtrl @Inject()(user: User, loginSession: LoginSession,
                                       consulting: Consulting, contractStory: ContractStory,
                                       commonUtil: CommonUtil) extends Controller  {

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
      Ok(views.html._A_admin_contract_story(page_data, user_data, commonUtil))
    else
      Redirect("/")
  }

  def contract_story_list(category: String, board_page: Int) = Action { request =>
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
    stories = contractStory.getContractStories(category.replace("@", ""), "", board_page)._1
    totalCount = contractStory.getContractStories(category.replace("@", ""), "", board_page)._2(0)(".total").toString.toInt
    val count = totalCount / contractStory.pageSize + (if (totalCount % contractStory.pageSize == 0) 0 else 1)

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_contract_story_list(page_data, user_data, commonUtil, stories, count, board_page))
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

    var story:Map[String, Any] = null

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_contract_stories_write(page_data, user_data, story))
    else
      Redirect("/")
  }

  def contract_story_modify(idx:String) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "contract_story"
    page_data += "left_menu" -> "contract_story"

    var story = Map[String, Any]()
    story = contractStory.getAContractStory(idx)

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_contract_stories_write(page_data, user_data, story))
    else
      Redirect("/")
  }

  def contract_story_submit(insertOrModify: Int) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)
    var client_id = ""
    request.session.get("user_id").map { id =>
      client_id = id
    }

    if (insertOrModify == 0) {
      val insert = contractStory.contractStoryWrite(fp.get("category"), fp.get("title"), fp.get("ir1"), fp.get("modified"))
      if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
        Ok(insert match {
          case Some(i: Long) => views.html.alert_and_move("게시물이 등록되었습니다.", "/admin/contract_story")
          case None => views.html.alert_and_move("에러가 발생하여 게시물이 등록되지 않앗습니다.", "/admin/contract_story_write")
        })
      else
        Redirect("/")
    }
    else {
      val modify = contractStory.contractStoryModify(insertOrModify.toString, fp.get("category"), fp.get("title"), fp.get("ir1"), fp.get("modified"))
      if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
        Ok(modify match {
          case 1 => views.html.alert_and_move("게시물이 수정되었습니다", "/admin/contract_story")
          case default => views.html.alert_and_move("에러가 발생하여 게시물이 수정되지 않앗습니다.  다시 시도해 주십시오.", "/admin/contract_story_write")
        })
      else
        Redirect("/")
    }
  }

}
