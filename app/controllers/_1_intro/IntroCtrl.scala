package controllers._1_intro

import javax.inject._

import controllers.LoginSession
import models.User
import play.api.mvc._

/**
  * Created by Hyeonmin on 2016-11-01.
  */
@Singleton
class IntroCtrl @Inject()(user: User, loginSession: LoginSession) extends Controller {

  def about = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3그룹은?"
    page_data += "login" -> ""
    page_data += "category" -> "intro"
    page_data += "page" -> "about"

    Ok(views.html._1_intro_01_about(page_data, user_data))
  }

}
