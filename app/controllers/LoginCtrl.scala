package controllers

import javax.inject._

import models.User
import play.api.mvc._

/**
  * Created by Hyeonmin on 2016-11-01.
  */
@Singleton
class LoginCtrl @Inject()(user: User) extends Controller {
  def login = Action { request =>

    val id = request.body.asFormUrlEncoded.get("id").head
    val pw = request.body.asFormUrlEncoded.get("pw").head

    var user_data_list = List[Map[String, Any]]()
    user_data_list = user.idPwCheck(id, pw)

    var user_data = Map[String, Any]()
    var user_id = ""
    var user_pw = ""
    if (user_data_list.length > 0) {
      user_data = user_data_list(0)
      user_data += ("logged_in" -> 1)
      user_id = id
      user_pw = pw
    } else {
      user_data += ("logged_in" -> 0)
    }

    Ok(scala.util.parsing.json.JSONObject(user_data).toString()).withSession(
      ("user_id" -> user_id),
      ("user_pw" -> user_pw)
    )
  }

  def logout = Action {
    Ok("LogOut").withSession()
  }
}
