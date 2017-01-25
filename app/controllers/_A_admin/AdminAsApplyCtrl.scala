package controllers._A_admin

import javax.inject._

import controllers.LoginSession
import models.{Consulting, Deleter, User}
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2017-01-25.
  */
class AdminAsApplyCtrl @Inject()(user: User, loginSession: LoginSession,
                                   consultingMdl: Consulting,
                                   deleter: Deleter,
                                   commonUtil: CommonUtil) extends Controller {

  def as_apply = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "as_apply"
    page_data += "left_menu" -> "as_apply"

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_as_apply(page_data, user_data, commonUtil))
    else
      Redirect("/")
  }

  def as_apply_list() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val board_page = request.body.asFormUrlEncoded.get("page").head.toInt

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "as_apply"
    page_data += "left_menu" -> "as_apply"

    var stories = List[Map[String, Any]]()
    var totalCount = 0

    val listAndCount = consultingMdl.getAsApplies(board_page)

    stories = listAndCount._1
    totalCount = listAndCount._2(0)(".total").toString.toInt
    val count = totalCount / consultingMdl.pageSize + (if (totalCount % consultingMdl.pageSize == 0) 0 else 1)

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_as_apply_list(page_data, user_data, commonUtil, stories, count, board_page))
    else
      Redirect("/")
  }

  def as_apply_view(idx:String) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "as_apply"
    page_data += "left_menu" -> "as_apply"

    var as_apply = Map[String, Any]()
    as_apply = consultingMdl.getAAsApply(idx)

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_as_apply_view(page_data, user_data, commonUtil, as_apply))
    else
      Redirect("/")
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
    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(result match {
        case 1 => views.html.alert_and_move("게시물이 삭제되었습니다", "/admin/as_apply")
        case default => views.html.alert_and_move("에러가 발생하여 게시물이 삭제되지 않았습니다.  다시 시도해 주십시오.", "/admin/as_apply_view")
      })
    else
      Redirect("/")
  }

}
