package controllers

import javax.inject._
import models.User
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2016-12-26.
  */
class UserCtrl @Inject()(user: User, commonUtil: CommonUtil, loginSession: LoginSession) extends Controller{
  def register = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "user"
    page_data += "page" -> "register"

    Ok(views.html._B_user_register(page_data, user_data))
  }

  def idCheck = Action { request =>
    val id = request.body.asFormUrlEncoded.get("id").head

    var user_data_list = List[Map[String, Any]]()
    user_data_list = user.idCheck(id)

    val result = user_data_list.length

    Ok(result.toString)
  }

  def registerSubmit = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)

    val user_birth: Array[String] = fp.get("user_birth").split("-")
    val user_phone = fp.get("user_phone_1") + "-" + fp.get("user_phone_2") + "-" +  fp.get("user_phone_3")
    val user_email = fp.get("user_email_1") + "@" + fp.get("user_email_2")

    val insert = user.registerUser(fp.get("user_id"), fp.get("user_pw"), fp.get("user_name"), user_birth(0), user_birth(1), user_birth(2), user_phone, user_email, fp.get("user_address_1"), fp.get("user_address_2"), fp.get("created"))
    println(insert)
    Ok(insert match {
      case Some(i: Long) => views.html.alert_and_move("가입이 완료되었습니다.  로그인해주세요.", "/")
      case None => views.html.alert_and_move("에러가 발생하여 가입이 완료되지 않았습니다.", "/user/register")
    })
  }

  def registerModify = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)

    val user_birth: Array[String] = fp.get("user_birth").split("-")
    val user_phone = fp.get("user_phone_1") + "-" + fp.get("user_phone_2") + "-" +  fp.get("user_phone_3")
    val user_email = fp.get("user_email_1") + "@" + fp.get("user_email_2")

    val modify = user.modifyUser(fp.get("user_id"), fp.get("user_pw"), fp.get("user_name"), user_birth(0), user_birth(1), user_birth(2), user_phone, user_email, fp.get("user_address_1"), fp.get("user_address_2"), fp.get("created"))
    Ok(modify match {
      case 1 => views.html.alert_and_move("회원정보가 수정되었습니다.", "/user/register")
      case default => views.html.alert_and_move("에러가 발생하여 회원정보가 수정되지 않았습니다.", "/user/register")
    })
  }

}
