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

  val letters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

  def createSalt(): String = {
    def addLetter(i: Int, s: String): String = {
      if (i > 0) {
        addLetter(i - 1, s + letters.charAt((Math.random() * letters.length).toInt))
      }
      else s
    }
    addLetter(20, "")
  }

  def passwordHashing(pw:String):String = {
    val m = java.security.MessageDigest.getInstance("MD5")
    val b = pw.getBytes("UTF-8")
    m.update(b, 0, b.length)
    new java.math.BigInteger(1, m.digest()).toString(16)
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

  def spaceStoryCategory(code: String): String =
    code match {
      case "1" => {"건축이 궁금해"}
      case "2" => {"자유로운 공간"}
    }

}
