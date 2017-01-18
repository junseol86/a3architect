package controllers._A_admin

import javax.inject._

import controllers.LoginSession
import models.{Project, Deleter, User}
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2017-01-10.
  */
class AdminProjectCtrl @Inject()(user: User, loginSession: LoginSession, projectMdl: Project, deleter: Deleter,
                              commonUtil: CommonUtil) extends Controller {

  def project = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "project"
    page_data += "left_menu" -> "project"

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_project(page_data, user_data, commonUtil))
    else
      Redirect("/")
  }

  def project_list() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val board_page = request.body.asFormUrlEncoded.get("page").head.toInt

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "project"
    page_data += "left_menu" -> "project"

    var projects = List[Map[String, Any]]()
    var totalCount = 0

    val listAndCount = projectMdl.getProjects(board_page)

    projects = listAndCount._1
    totalCount = listAndCount._2(0)(".total").toString.toInt
    val count = totalCount / projectMdl.pageSize + (if (totalCount % projectMdl.pageSize == 0) 0 else 1)

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_project_list(page_data, user_data, commonUtil, projects, count, board_page))
    else
      Redirect("/")
  }

  def project_write = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "project"
    page_data += "left_menu" -> "project"

    var project:Map[String, Any] = null

    var options = Map[String, List[Map[String, Any]]]()
    options = projectMdl.getOptions

    var children = Map[String, Any]()
    children = null

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_project_write(page_data, user_data, project, options, children))
    else
      Redirect("/")
  }

  def project_modify(idx:String) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "project"
    page_data += "left_menu" -> "project"

    var project = Map[String, Any]()
    project = projectMdl.getAProject(idx)

    var options = Map[String, List[Map[String, Any]]]()
    options = projectMdl.getOptions

    val childrenRaw = projectMdl.projectChildren(idx)
    var children = Map[String, Any]()
    children += "construction" -> childrenRaw._1.toString
    children += "design" -> childrenRaw._2.toString
    children += "interior" -> childrenRaw._3.toString
    children += "sites" -> childrenRaw._4.toString

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_project_write(page_data, user_data, project, options, children))
    else
      Redirect("/")
  }

  def project_submit(insertOrModify: Int) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)
    var client_id = ""
    request.session.get("user_id").map { id =>
      client_id = id
    }

    if (insertOrModify == 0) {
      val insert = projectMdl.projectWrite(
        fp.get("title"), fp.get("subtitle"), fp.get("client_name"), fp.get("client_name"),
        fp.get("yongdo"), fp.get("gujo"), fp.get("jaeryo"),
        fp.get("year"), fp.get("location"),
        fp.get("daeji"), fp.get("gunchuk"), fp.get("yeon"),
        fp.get("gyumo"), fp.get("state"),
        fp.get("modified"),
        fp.get("hashtag")
      )
      if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
        Ok(insert match {
          case Some(i: Long) => views.html.alert_and_move("게시물이 등록되었습니다.", "/admin/project")
          case None => views.html.alert_and_move("에러가 발생하여 게시물이 등록되지 않았습니다.", "/admin/project_write")
        })
      else
        Redirect("/")
    }
    else {
      val modify = projectMdl.projectModify(insertOrModify.toString,
        fp.get("title"), fp.get("subtitle"), fp.get("client_name"), fp.get("client_name"),
        fp.get("yongdo"), fp.get("gujo"), fp.get("jaeryo"),
        fp.get("year"), fp.get("location"),
        fp.get("daeji"), fp.get("gunchuk"), fp.get("yeon"),
        fp.get("gyumo"), fp.get("state"),
        fp.get("modified"),
        fp.get("hashtag")
      )

      if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
        Ok(modify match {
          case 1 => views.html.alert_and_move("게시물이 수정되었습니다", "/admin/project")
          case default => views.html.alert_and_move("에러가 발생하여 게시물이 수정되지 않았습니다.  다시 시도해 주십시오.", "/admin/project_write")
        })
      else
        Redirect("/")
    }
  }

}
