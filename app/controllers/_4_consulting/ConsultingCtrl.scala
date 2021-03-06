package controllers._4_consulting

import javax.inject._

import controllers.LoginSession
import models.{Consulting, ContractStory, Deleter, User}
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2016-11-22.
  */

@Singleton
class ConsultingCtrl @Inject()(user: User, loginSession: LoginSession,
                               consulting: Consulting, contractStory: ContractStory,
                               deleter: Deleter,
                               commonUtil: CommonUtil) extends Controller {

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

    val insert = consulting.con_apply(category, client_id, client_name, client_phone, client_email,
      address_1, address_2, address_3, purpose, pyung, floor, base_floor,
      yongdo_main, yongdo_sub, gujo_main, gujo_sub, style,
      meeting_from, meeting_to, meeting_hour,
      budget, constuction_date, others,
      created)
    val insertResult = insert match {
      case None => ""
      case Some(i: Long) => i.toString
    }

    Ok(insert match {
      case None => views.html.alert_and_move("신청서가 접수되지 않았습니다.  다시 시도해 주십시오.", "/consulting/con_apply")
      case Some(i: Long) => views.html.alert_and_move("신청서가 접수되었습니다.  곧 연락드리겠습니다.", "/")
    })
  }

  def contract_story() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 계약 스토리"
    page_data += "login" -> ""
    page_data += "category" -> "consulting"
    page_data += "page" -> "contract_story"

    Ok(views.html._4_consulting_03_contract_story(page_data, user_data))
  }

  def contract_story_list() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val category = request.body.asFormUrlEncoded.get("category").head
    val board_page = request.body.asFormUrlEncoded.get("page").head.toInt

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 계약 스토리"
    page_data += "login" -> ""
    page_data += "category" -> "consulting"
    page_data += "page" -> "contract_story"

    var stories = List[Map[String, Any]]()
    var totalCount = 0

    val listAndCount = contractStory.getContractStories(category.replace("@", ""), "", board_page)

    stories = listAndCount._1
    totalCount = listAndCount._2(0)(".total").toString.toInt
    val count = totalCount / contractStory.pageSize + (if (totalCount % contractStory.pageSize == 0) 0 else 1)

    Ok(views.html._4_consulting_03_contract_story_list(page_data, user_data, commonUtil, stories, count, board_page))
  }

  def contract_story_view(idx: String) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 계약 스토리"
    page_data += "login" -> ""
    page_data += "category" -> "consulting"
    page_data += "page" -> "contract_story"

    var story = Map[String, Any]()
    story = contractStory.getAContractStory(idx)

    Ok(views.html._4_consulting_03_contract_story_view(page_data, user_data, commonUtil, story))
  }

  def as_apply = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: AS 신청"
    page_data += "login" -> ""
    page_data += "category" -> "consulting"
    page_data += "page" -> "as_apply"

    var options = Map[String, List[Map[String, Any]]]()
    options = consulting.getOptions

    Ok(views.html._4_consulting_04_as_apply(page_data, user_data))
  }

  def as_apply_list() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val board_page = request.body.asFormUrlEncoded.get("page").head.toInt

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 계약 스토리"
    page_data += "login" -> ""
    page_data += "category" -> "consulting"
    page_data += "page" -> "as_apply"

    var stories = List[Map[String, Any]]()
    var totalCount = 0

    val listAndCount = consulting.getAsApplies(board_page)

    stories = listAndCount._1
    totalCount = listAndCount._2(0)(".total").toString.toInt
    val count = totalCount / contractStory.pageSize + (if (totalCount % contractStory.pageSize == 0) 0 else 1)

    Ok(views.html._4_consulting_04_as_apply_list(page_data, user_data, commonUtil, stories, count, board_page))
  }


  def as_apply_submit = Action { request =>

    val fp = new commonUtil.FromPost(request)

    var client_id = ""
    request.session.get("user_id").map { id =>
      client_id = id
    }
    val client_name = fp.get("client_name")
    val client_phone =
      fp.get("client_phone_1") + "-" + fp.get("client_phone_2") + "-" + fp.get("client_phone_3")
    val title = fp.get("title")
    val subtitle = fp.get("subtitle")
    val thumbnail = fp.get("thumbnail")
    val term = fp.get("term")
    val created = fp.get("created")

    val insert = consulting.as_apply(client_id, client_name, client_phone, title, subtitle, created, thumbnail, term)
    val insertResult = insert match {
      case None => ""
      case Some(i: Long) => i.toString
    }

    Ok(insert match {
      case None => views.html.alert_and_move("신청서가 접수되지 않았습니다.  다시 시도해 주십시오.", "/consulting/as_apply")
      case Some(i: Long) => views.html.alert_and_move("신청서가 접수되었습니다.", "/consulting/as_apply")
    })
  }

  def as_apply_delete(idx: String) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)
    var client_id = ""
    request.session.get("user_id").map { id =>
      client_id = id
    }

    val result = deleter.deleteAContent("tbl_as_apply", "aa_idx", idx)
    if (user_data.length > 0)
      Ok(result match {
        case 1 => views.html.alert_and_move("게시물이 삭제되었습니다", "/consulting/as_apply")
        case default => views.html.alert_and_move("에러가 발생하여 게시물이 삭제되지 않았습니다.  다시 시도해 주십시오.", "/consulting/as_apply")
      })
    else
      Redirect("/")
  }
}
