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
    projects = projectMdl.getProjects(board_page)._1
    totalCount = projectMdl.getProjects(board_page)._2(0)(".total").toString.toInt
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

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_project_write(page_data, user_data, project, options))
    else
      Redirect("/")
  }

}
