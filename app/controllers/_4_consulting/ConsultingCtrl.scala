package controllers._4_consulting

import javax.inject._

import controllers.LoginSession
import models.{Consulting, User}
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2016-11-22.
  */

@Singleton
class ConsultingCtrl @Inject()(user: User, loginSession: LoginSession, consulting: Consulting, commonUtil: CommonUtil) extends Controller {

  def profess = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 진행절차"
    page_data += "login" -> ""
    page_data += "category" -> "consulting"
    page_data += "page" -> "process"

    Ok(views.html._4_consulting_01_process(page_data, user_data))
  }

  def con_apply = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 컨설팅 신청"
    page_data += "login" -> ""
    page_data += "category" -> "consulting"
    page_data += "page" -> "con_apply"

    var options = Map[String, List[Map[String, Any]]]()
    options = consulting.getOptions

    if (user_data.length != 0)
      Ok(views.html._4_consulting_02_con_apply(page_data, user_data, options, commonUtil))
    else
      Ok(views.html.go_back(page_data, user_data))
  }

  def con_apply_submit = Action { request =>
    println("hahaha")
    val client_name = request.body.asFormUrlEncoded.get("client_name").head
    println(client_name)
    Ok(client_name)
  }

}
