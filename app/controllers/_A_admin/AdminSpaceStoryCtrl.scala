package controllers._A_admin

import javax.inject._

import controllers.LoginSession
import models.{Consulting, Deleter, SpaceStory, User}
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2016-11-28.
  */
class AdminSpaceStoryCtrl @Inject()(user: User, loginSession: LoginSession,
                                    consulting: Consulting, spaceStoryMdl: SpaceStory,
                                    deleter: Deleter,
                                    commonUtil: CommonUtil) extends Controller  {

  def space_story = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "space_story"
    page_data += "left_menu" -> "space_story"

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_space_story(page_data, user_data, commonUtil))
    else
      Redirect("/")
  }

  def space_story_list() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val category = request.body.asFormUrlEncoded.get("category").head
    val board_page = request.body.asFormUrlEncoded.get("page").head.toInt

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "space_story"
    page_data += "left_menu" -> "space_story"

    var stories = List[Map[String, Any]]()
    var totalCount = 0

    val listAndCount = spaceStoryMdl.getSpaceStories(category.replace("@", ""), board_page)

    stories = listAndCount._1
    totalCount = listAndCount._2(0)(".total").toString.toInt
    val count = totalCount / spaceStoryMdl.pageSize + (if (totalCount % spaceStoryMdl.pageSize == 0) 0 else 1)

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_space_story_list(page_data, user_data, commonUtil, stories, count, board_page))
    else
      Redirect("/")
  }

  def space_story_write = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "space_story"
    page_data += "left_menu" -> "space_story"

    var story:Map[String, Any] = null

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_space_story_write(page_data, user_data, commonUtil, story))
    else
      Redirect("/")
  }

  def space_story_modify(idx:String) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "space_story"
    page_data += "left_menu" -> "space_story"

    var story = Map[String, Any]()
    story = spaceStoryMdl.getASpaceStory(idx)

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_space_story_write(page_data, user_data, commonUtil, story))
    else
      Redirect("/")
  }

  def space_story_submit(insertOrModify: Int) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)
    var client_id = ""
    request.session.get("user_id").map { id =>
      client_id = id
    }

    if (insertOrModify == 0) {
      val insert = spaceStoryMdl.space_storyWrite(fp.get("category"), fp.get("title"),
        fp.get("thumbnail"), fp.get("ir1"), fp.get("modified"))
      if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
        Ok(insert match {
          case Some(i: Long) => views.html.alert_and_move("게시물이 등록되었습니다.", "/admin/space_story")
          case None => views.html.alert_and_move("에러가 발생하여 게시물이 등록되지 않았습니다.", "/admin/space_story_write")
        })
      else
        Redirect("/")
    }
    else {
      val modify = spaceStoryMdl.space_storyModify(insertOrModify.toString, fp.get("category"), fp.get("title"),
        fp.get("thumbnail"), fp.get("ir1"), fp.get("modified"))
      if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
        Ok(modify match {
          case 1 => views.html.alert_and_move("게시물이 수정되었습니다", "/admin/space_story")
          case default => views.html.alert_and_move("에러가 발생하여 게시물이 수정되지 않았습니다.  다시 시도해 주십시오.", "/admin/space_story_write")
        })
      else
        Redirect("/")
    }
  }

  def space_story_delete(idx: String) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)
    var client_id = ""
    request.session.get("user_id").map { id =>
      client_id = id
    }

    val result = deleter.deleteAContent("tbl_space_story", "ss_idx", idx)
    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(result match {
        case 1 => views.html.alert_and_move("게시물이 삭제되었습니다", "/admin/space_story")
        case default => views.html.alert_and_move("에러가 발생하여 게시물이 삭제되지 않았습니다.  다시 시도해 주십시오.", "/admin/space_story_write")
      })
    else
      Redirect("/")
  }

}
