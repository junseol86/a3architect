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

    val fp = new commonUtil.FromPost(request)

    val category = fp.get("category")
    val lock = fp.get("lock")
    var client_id = ""
    request.session.get("user_id").map { id =>
      client_id = id
    }
    val client_name = fp.get("client_name")
    val client_phone =
      fp.get("client_phone_1") + "-" + fp.get("client_phone_2") + "-" + fp.get("client_phone_3")
    val client_email = fp.get("client_email_1") + "@" + fp.get("client_email_2")
    val address_1 = fp.get("address_1")
    val address_2 = fp.get("address_2")
    val address_3 = fp.get("address_3")
    val purpose = fp.get("purpose")
    val pyung = fp.get("pyung")
    val floor = fp.get("floor")
    val base_floor = fp.get("base_floor")
    val yongdo_main = fp.get("yongdo_main")
    val yongdo_sub = fp.get("yongdo_sub")
    val gujo_main = fp.get("gujo_main")
    val gujo_sub = fp.get("gujo_sub")
    val style = fp.get("style")
    val meeting_from = fp.get("meeting_from")
    val meeting_to = fp.get("meeting_to")
    val meeting_hour = fp.get("meeting_hour")
    val budget = fp.get("budget")
    val constuction_date = fp.get("construction_date")
    val others = fp.get("others")
    val created = fp.get("created")
    val modified = fp.get("created")

    val insert = consulting.con_apply(category, lock, client_id, client_name, client_phone, client_email, address_1, address_2, address_3, purpose, pyung, floor, base_floor, yongdo_main, yongdo_sub, gujo_main, gujo_sub, style, meeting_from, meeting_to, meeting_hour, budget, constuction_date, others, modified, created)
    val insertResult = insert match {
      case None => ""
      case Some(i: Long) => i.toString
    }

    Ok(insert match {
      case None => ""
      case Some(i: Long) => i.toString
    })
  }

  def contract_stories_forward = Action {
    Redirect("/consulting/contract_story/1/@/1")
  }

  def contract_stories(category: Int, search: String, page: Int) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 계약 스토리"
    page_data += "login" -> ""
    page_data += "category" -> "consulting"
    page_data += "page" -> "contract_story"

    var list_data = Map[String, Any]()
    list_data += "category" -> category.toString
    list_data += "search" -> search
    list_data += "page" -> page.toString

    Ok(views.html._4_consulting_03_contract_story(page_data, user_data, list_data))
  }
}
