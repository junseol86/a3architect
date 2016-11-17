package controllers._1_intro

import javax.inject._

import controllers.LoginSession
import models.{People, User}
import play.api.mvc._

/**
  * Created by Hyeonmin on 2016-11-01.
  */
@Singleton
class IntroCtrl @Inject()(user: User, loginSession: LoginSession, ppl: People) extends Controller {

  def about = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: A3그룹은?"
    page_data += "login" -> ""
    page_data += "category" -> "intro"
    page_data += "page" -> "about"

    Ok(views.html._1_intro_01_about(page_data, user_data))
  }

  def people = Action { request =>
    var user_data = List[Map[String, Any]]()
    user_data = loginSession.userData(request)

    var people_data = List[Map[String, Any]]()
    people_data = ppl.getList

    var page_data = Map[String, Any]()
    page_data += "title" -> "A3 :: A3 사람들"
    page_data += "login" -> ""
    page_data += "category" -> "intro"
    page_data += "page" -> "people"

    Ok(views.html._1_intro_02_people(page_data, user_data, people_data))
  }

}
