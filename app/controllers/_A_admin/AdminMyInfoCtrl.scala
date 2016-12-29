package controllers._A_admin

import javax.inject._

import controllers.LoginSession
import models.{User}
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2016-11-28.
  */
class AdminMyInfoCtrl @Inject()(user: User, loginSession: LoginSession,
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
      Redirect("/user/register")
    else
      Redirect("/")
  }

}
