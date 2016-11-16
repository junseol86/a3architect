package controllers

import play.api.mvc.Request
import models.User
import javax.inject._

/**
  * Created by Hyeonmin on 2016-11-16.
  */
class LoginSession @Inject()(user: User)  {
//  페이지마다 세션에서 ID와 비번 값을 가져와 로그인 여부를 결정

  def userData(request: Request[Any]): List[Map[String, Any]] = {
    var user_id = ""
    var user_pw = ""

    request.session.get("user_id").map { id =>
      user_id = id
    }.getOrElse {
      user_id = ""
    }

    request.session.get("user_pw").map { pw =>
      user_pw = pw
    }.getOrElse {
      user_pw = ""
    }

    user.idPwCheck(user_id, user_pw)
  }

}
