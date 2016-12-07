package controllers

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

import play.api.libs.Files
import play.api.mvc._

/**
  * Created by Hyeonmin on 2016-12-07.
  */
class UploadCtrl extends Controller {

  def setFileName(picture: MultipartFormData.FilePart[Files.TemporaryFile]):String =  {
    val format = new SimpleDateFormat("yyyy-mm-dd_HH-mm-ss")
    val filenameFront = format.format(new Date()) + "_" +
      (99999 - Math.random()*100000).toInt.toString
      filenameFront + "." + picture.filename.split('.').last
  }

  def contractStoryImage = Action(parse.multipartFormData) { request =>
    println("hahahahhahahaha")
    request.body.file("picture").map { picture =>
      val filename = setFileName(picture)
      val url = s"/images/contents/contract_story/$filename"
      picture.ref.moveTo(new File(s"public$url"))
      Ok(url)
    }.getOrElse { Ok("Error") }
  }

}
