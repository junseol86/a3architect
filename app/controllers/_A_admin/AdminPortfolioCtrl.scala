package controllers._A_admin

import javax.inject._

import controllers.LoginSession
import models.{Portfolio, Deleter, User}
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2017-01-11.
  */
class AdminPortfolioCtrl @Inject()(user: User, loginSession: LoginSession,
                                   portfolioMdl: Portfolio, deleter: Deleter,
                                   commonUtil: CommonUtil) extends Controller {

  def portfolio_write(pj_idx: String, pf_category: String) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "portfolio"
    page_data += "left_menu" -> "project"
    page_data += "pf_category" -> pf_category
    page_data += "pj_idx" -> pj_idx

    var portfolio:Map[String, Any] = null

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_portfolio_write(page_data, user_data, commonUtil, portfolio))
    else
      Redirect("/")
  }

  def portfolio_modify(pj_idx:String, pf_category: String) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "portfolio"
    page_data += "left_menu" -> "portfolio"
    page_data += "pf_category" -> pf_category
    page_data += "pj_idx" -> pj_idx

    var portfolio = Map[String, Any]()
    portfolio = portfolioMdl.getAPortfolio(pj_idx, pf_category)

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_portfolio_write(page_data, user_data, commonUtil, portfolio))
    else
      Redirect("/")
  }

  def portfolio_submit(insertOrModify: Int) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)
    var client_id = ""
    request.session.get("user_id").map { id =>
      client_id = id
    }

    if (insertOrModify == 0) {
      val insert = portfolioMdl.portfolioWrite(fp.get("pj_idx"), fp.get("ir1"), fp.get("thumbnail"), fp.get("modified"), fp.get("pf_category"), fp.get("in_charge"), fp.get("in_charge_from"), fp.get("in_charge_photo"))
      if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
        Ok(insert match {
          case Some(i: Long) => views.html.alert_and_move("게시물이 등록되었습니다.", "/admin/project_modify/" + fp.get("pj_idx"))
          case None => views.html.alert_and_move("에러가 발생하여 게시물이 등록되지 않았습니다.", "/admin/portfolio_write")
        })
      else
        Redirect("/")
    }
    else {
      val modify = portfolioMdl.portfolioModify(insertOrModify.toString, fp.get("ir1"), fp.get("thumbnail"), fp.get("modified"), fp.get("in_charge"), fp.get("in_charge_from"), fp.get("in_charge_photo"))
      if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
        Ok(modify match {
          case 1 => views.html.alert_and_move("게시물이 수정되었습니다", "/admin/project_modify/" + fp.get("pj_idx"))
          case default => views.html.alert_and_move("에러가 발생하여 게시물이 수정되지 않았습니다.  다시 시도해 주십시오.", "/admin/portfolio_write")
        })
      else
        Redirect("/")
    }
  }
  

  def portfolioPicsList() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)
    val pj_idx = fp.get("pj_idx")
    val pf_category = fp.get("pf_category")

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: 관리자 페이지"
    page_data += "login" -> ""
    page_data += "category" -> "admin"
    page_data += "page" -> "portfolio"
    page_data += "left_menu" -> "project"
    page_data += "pf_category" -> pf_category
    page_data += "pj_idx" -> pj_idx

    var pics = List[Map[String, Any]]()
    pics = portfolioMdl.getPortfolioPics(pj_idx, pf_category)

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(views.html._A_admin_portfolio_pic_list(page_data, user_data, pics))
    else
      Redirect("/")
  }

  def portfolioPicsUpload() = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)

    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(portfolioMdl.portfolioPicsWrite(fp.get("pj_idx"), fp.get("pf_category"), fp.get("urls")))
    else
      Redirect("/")
  }

  def portfolioPicDelete = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)
    var client_id = ""
    request.session.get("user_id").map { id =>
      client_id = id
    }

    val result = deleter.deleteAContent("tbl_portfolio_pic", "pfp_idx", fp.get("idx"))
    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(result match {
        case 1 => "success"
        case default => "fail"
      })
    else
      Redirect("/")
  }

  def portfolio_delete(idx: String, pj_idx: String) = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    val fp = new commonUtil.FromPost(request)
    var client_id = ""
    request.session.get("user_id").map { id =>
      client_id = id
    }

    val result = deleter.deleteAContent("tbl_portfolio", "pf_idx", idx)
    if (user_data.length > 0 && user_data(0)("tbl_user.user_group") == "ADMIN")
      Ok(result match {
        case 1 => views.html.alert_and_move("게시물이 삭제되었습니다", "/admin/project_modify/" + pj_idx)
        case default => views.html.alert_and_move("에러가 발생하여 게시물이 삭제되지 않았습니다.  다시 시도해 주십시오.", "/admin/project_modify/" + pj_idx)
      })
    else
      Redirect("/")
  }

}
