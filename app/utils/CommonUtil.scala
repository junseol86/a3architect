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

}