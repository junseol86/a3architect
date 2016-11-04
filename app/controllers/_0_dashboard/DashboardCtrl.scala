package controllers._0_dashboard

import javax.inject._
import play.api.mvc._
import models.User

/**
  * Created by Hyeonmin on 2016-11-01.
  */
@Singleton
class DashboardCtrl @Inject()(user: User) extends Controller {


  def index = Action {
    var page_data = Map[String, Any]()
    page_data += "title" -> "A3그룹은?"
    page_data += "login" -> ""
    page_data += "category" -> ""
    page_data += "page" -> ""

    var user_data = List[Map[String, Any]]()
    user_data = user.getList
    Ok(views.html._0_dashboard(page_data, user_data))
  }
}
