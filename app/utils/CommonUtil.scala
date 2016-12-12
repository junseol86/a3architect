package utils

import play.api.mvc._

/**
  * Created by Hyeonmin on 2016-11-24.
  */
class CommonUtil {

//  포스트 요청으로 받은 데이터 처리
  class FromPost(request: Request[AnyContent]) {
    def get(str: String) =
      request.body.asFormUrlEncoded.get(str).head
  }

  def contractStoryCategory(code: String): String =
    code match {
      case "1" => {"BRAND"}
      case "2" => {"건축"}
      case "3" => {"인테리어"}
      case "4" => {"시공"}
    }

  def promotionCategory(code: String): String =
    code match {
      case "1" => {"NOW"}
      case "2" => {"PAST"}
      case "3" => {"FUTURE"}
    }

}
