package models

import javax.inject._
import play.api.db._
import anorm._
import anorm.{ RowParser, SqlParser }
import utils.CommonUtil

class User @Inject()(db: Database, commonUtil: CommonUtil) {

  val parser: RowParser[Map[String, Any]] =
    SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
      Right(map + (meta.column.qualified -> value))
    }

  def idPwCheck(id: String, pw: String) = {
    var saltRaw = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      saltRaw = SQL(
        s"""SELECT tbl_user.user_salt
           |FROM tbl_user
           |WHERE user_id = '$id'
         """.stripMargin
      ).as(parser.*)
    }

    val salt = if (saltRaw.length > 0) saltRaw(0)("tbl_user.user_salt") else ""
    val hash = commonUtil.passwordHashing(pw + salt)

    var result = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      result = SQL(
        s"""SELECT
           |tbl_user.user_id,
           |tbl_user.user_group,
           |tbl_user.user_name,
           |tbl_user.user_mobile,
           |tbl_user.user_email,
           |tbl_user.user_addr,
           |tbl_user.user_birth_year,
           |tbl_user.user_birth_month,
           |tbl_user.user_birth_day
           |FROM tbl_user
           |WHERE user_id = '$id'
           |AND user_pwd = '$hash'
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
