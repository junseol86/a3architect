package controllers._1_intro

import javax.inject._

import play.api.mvc._

/**
  * Created by Hyeonmin on 2016-11-01.
  */
@Singleton
class IntroCtrl @Inject() extends Controller {

  def about = Action {
    var page_data = Map[String, Any]()
    page_data += "title" -> "A3그룹은?"
    page_data += "login" -> ""
    page_data += "category" -> "intro"
    page_data += "page" -> "about"
    Ok(views.html._1_intro_01_about(page_data))
  }

}
