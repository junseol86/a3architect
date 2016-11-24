package models

import javax.inject._
import play.api.db._
import anorm._
import anorm.{ RowParser, SqlParser }

class User @Inject()(db: Database) {

  val parser: RowParser[Map[String, Any]] =
    SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
      Right(map + (meta.column.qualified -> value))
    }

  def idPwCheck(id: String, pw: String) = {
    var result = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      result = SQL(
        s"""SELECT
           |user_id,
           |user_name,
           |user_mobile,
           |user_email,
           |user_group
           |FROM tbl_user
           |WHERE user_id = '$id'
           |AND user_pwd = '$pw'
           |""".stripMargin).as(parser.*)
    }
    result
  }

  def getList = {
    var result = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      result = SQL("""SELECT * FROM tbl_user""").as(parser.*)
    }
    result
  }
}
