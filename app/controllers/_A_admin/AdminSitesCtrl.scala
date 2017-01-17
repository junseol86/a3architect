package controllers._A_admin

import javax.inject._

import controllers.LoginSession
import models.{Sites, Deleter, User}
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2017-01-16.
  */
class AdminSitesCtrl @Inject()(user: User, loginSession: LoginSession,
                                   sitesMdl: Sites, deleter: Deleter,
                                   commonUtil: CommonUtil) extends Controller {


  def sites_write(pj_idx: String) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "sites"
    page_data += "left_menu" -> "project"
    page_data += "pj_idx" -> pj_idx

    var sites:Map[String, Any] = null

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_sites_write(page_data, user_data, commonUtil, sites))
    else
      Redirect("/")
  }

  def sites_modify(pj_idx:String) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "sites"
    page_data += "left_menu" -> "sites"
    page_data += "pj_idx" -> pj_idx

    var sites = Map[String, Any]()
    sites = sitesMdl.getASites(pj_idx)

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_sites_write(page_data, user_data, commonUtil, sites))
    else
      Redirect("/")
  }

  def sites_submit(insertOrModify: Int) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)
    var client_id = ""
    request.session.get("user_id").map { id =>
      client_id = id
    }

    if (insertOrModify == 0) {
      val insert = sitesMdl.sitesWrite(fp.get("pj_idx"), fp.get("thumbnail"), fp.get("modified"), fp.get("sites_category"), fp.get("in_charge"), fp.get("in_charge_from"), fp.get("in_charge_photo"))
      if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
        Ok(insert match {
          case Some(i: Long) => views.html.alert_and_move("게시물이 등록되었습니다.", "/admin/project_modify/" + fp.get("pj_idx"))
          case None => views.html.alert_and_move("에러가 발생하여 게시물이 등록되지 않았습니다.", "/admin/sites_write")
        })
      else
        Redirect("/")
    }
    else {
      val modify = sitesMdl.sitesModify(insertOrModify.toString, fp.get("thumbnail"), fp.get("modified"), fp.get("sites_category"), fp.get("in_charge"), fp.get("in_charge_from"), fp.get("in_charge_photo"))
      if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
        Ok(modify match {
          case 1 => views.html.alert_and_move("게시물이 수정되었습니다", "/admin/project_modify/" + fp.get("pj_idx"))
          case default => views.html.alert_and_move("에러가 발생하여 게시물이 수정되지 않았습니다.  다시 시도해 주십시오.", "/admin/sites_write")
        })
      else
        Redirect("/")
    }
  }

  def sitesSceneList() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)
    val pj_idx = fp.get("pj_idx")

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "portfolio"
    page_data += "left_menu" -> "project"
    page_data += "pj_idx" -> pj_idx

    var pics = List[Map[String, Any]]()
    pics = sitesMdl.getSitesScenes(pj_idx)

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_sites_scene_list(page_data, user_data, pics))
    else
      Redirect("/")
  }

  def sitesSceneUpload() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(sitesMdl.sitesSceneWrite(fp.get("pj_idx"), fp.get("category"), fp.get("title"), fp.get("urls")).toString())
    else
      Redirect("/")
  }

  def sitesSceneDelete = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)
    var client_id = ""
    request.session.get("user_id").map { id =>
      client_id = id
    }

    val result = deleter.deleteAContent("tbl_sites_pic", "stsp_idx", fp.get("idx"))
    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(result match {
        case 1 => "success"
        case default => "fail"
      })
    else
      Redirect("/")
  }

}
