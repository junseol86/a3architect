package controllers._A_admin

import javax.inject._

import controllers.LoginSession
import models.User
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2016-12-28.
  */
class AdminUserCtrl @Inject()(userMdl: User, commonUtil: CommonUtil, loginSession: LoginSession) extends Controller {
  def user = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "user"
    page_data += "left_menu" -> "user"

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_user(page_data, user_data, commonUtil))
    else
      Redirect("/")
  }

  def user_list() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val board_page = request.body.asFormUrlEncoded.get("page").head.toInt

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "user"
    page_data += "left_menu" -> "user"

    var users = List[Map[String, Any]]()
    var totalCount = 0
    users = userMdl.userList("", board_page)._1
    totalCount = userMdl.userList("", board_page)._2(0)(".total").toString.toInt
    val count = totalCount / userMdl.pageSize + (if (totalCount % userMdl.pageSize == 0) 0 else 1)

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_user_list(page_data, user_data, commonUtil, users, count, board_page))
    else
      Redirect("/")
  }

  def user_sts_toggle()  = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN") {
      var sts = List[Map[String, Any]]()
      sts = userMdl.stsToggle(fp.get("id"))
      Ok(sts(0)("tbl_user.sts").toString)
    }
    else
      Redirect("/")
  }
  
}
