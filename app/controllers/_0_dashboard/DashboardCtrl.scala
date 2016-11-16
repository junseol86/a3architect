package controllers._0_dashboard

import javax.inject._

import controllers.LoginSession
import play.api.mvc._
import models.User

/**
  * Created by Hyeonmin on 2016-11-01.
  */
@Singleton
class DashboardCtrl @Inject()(user: User, loginSession: LoginSession) extends Controller {


  def index = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3그룹은?"
    page_data += "category" -> ""
    page_data += "page" -> ""

//    var user_id = ""
//    var user_pw = ""
//
//    request.session.get("user_id").map { id =>
//      user_id = id
//    }.getOrElse {
//      user_id = ""
//    }
//
//    request.session.get("user_pw").map { pw =>
//      user_pw = pw
//    }.getOrElse {
//      user_pw = ""
//    }


    Ok(views.html._0_dashboard(page_data, user_data))
  }
}
