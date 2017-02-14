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

  val pageSize = 5

  def userList(search: String, page: Int) = {
    val pageOffset = (page - 1) * pageSize

    var list = List[Map[String, Any]]()
    var count = List[Map[String, Any]]()

    val commonQuery =
      f"""FROM tbl_user
         WHERE (
         user_id LIKE "%%$search%s%%"
         OR user_id LIKE "%%$search%s%%"
         )
         AND user_group = "HOST"
        """
    val listQuery =
      f"""SELECT *
         $commonQuery%s
         ORDER BY idx DESC
         LIMIT $pageOffset%d, $pageSize%d"""
    val countQuery =
      f"""SELECT count(*) as total $commonQuery%s"""
    db.withConnection{implicit conn =>
      list = SQL(
        listQuery.stripMargin).as(parser.*)
    }
    db.withConnection{implicit conn =>
      count = SQL(
        countQuery.stripMargin).as(parser.*)
    }
    (list, count)
  }

  def idCheck(id: String) = {
    var result = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      result = SQL(
        s"""SELECT tbl_user.user_id
           |FROM tbl_user
           |WHERE user_id = '$id'
         """.stripMargin
      ).as(parser.*)
    }
    result
  }


  def idPwCheck(id: String, pw: String) = {
    var saltRaw = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      saltRaw = SQL(
        s"""SELECT tbl_user.user_salt
           |FROM tbl_user
           |WHERE user_id = '$id'
           |AND sts = 'I'
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
           |tbl_user.user_addr2,
           |tbl_user.user_birth_year,
           |tbl_user.user_birth_month,
           |tbl_user.user_birth_day,
           |tbl_user.ssn_1,
           |tbl_user.ssn_2
           |FROM tbl_user
           |WHERE user_id = '$id'
           |AND user_pwd = '$hash'
           |AND sts = 'I'
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

  def registerUser(
                  id: String, password: String, name: String, birthYear: String, birthMonth: String, birthDay: String, phone: String, email: String, address1: String, address2: String, date: String, ssn_1: String, ssn_2: String
                  ) = {
    val salt = commonUtil.createSalt()
    val hashedPw = commonUtil.passwordHashing(password + salt)

    db.withConnection { implicit conn =>
      SQL(
        """
          INSERT INTO tbl_user (
          user_id, user_pwd, user_name, user_group, sts, user_mobile, user_email, cdate, udate, user_birth_year, user_birth_month, user_birth_day, user_addr, user_addr2, user_salt, ssn_1, ssn_2
          ) values (
          {id}, {pwd}, {name}, {group}, {sts}, {phone}, {email}, {date}, {date}, {birthYear}, {birthMonth}, {birthDay}, {address1}, {address2}, {salt}, {ssn_1}, {ssn_2}
          )
      """
      )
        .on(
          'id -> id, 'pwd -> hashedPw, 'name -> name, 'group -> "HOST", 'sts -> "I", 'phone -> phone, 'email -> email, 'date -> date, 'date -> date, 'birthYear -> birthYear, 'birthMonth -> birthMonth, 'birthDay -> birthDay, 'address1 -> address1, 'address2 -> address2, 'salt -> salt,
          'ssn_1 -> ssn_1, 'ssn_2 -> ssn_2
        ).executeInsert()
    }
  }

  def modifyUser(
                    id: String, password: String, name: String, birthYear: String, birthMonth: String, birthDay: String, phone: String, email: String, address1: String, address2: String, date: String, ssn_1: String, ssn_2: String
                  ) = {
    val salt = commonUtil.createSalt()
    val hashedPw = commonUtil.passwordHashing(password + salt)

    db.withConnection { implicit conn =>
      SQL(
        """
          UPDATE tbl_user SET
          user_pwd = {pwd}, user_name = {name}, user_mobile = {phone}, user_email = {email}, udate = {date}, user_birth_year = {birthYear}, user_birth_month = {birthMonth}, user_birth_day = {birthDay}, user_addr = {address1}, user_addr2 = {address2}, user_salt = {salt},
          ssn_1 = {ssn_1}, ssn_2 = {ssn_2}
          WHERE user_id = {id}
      """
      )
        .on(
          'id -> id, 'pwd -> hashedPw, 'name -> name, 'phone -> phone, 'email -> email, 'date -> date, 'date -> date, 'birthYear -> birthYear, 'birthMonth -> birthMonth, 'birthDay -> birthDay, 'address1 -> address1, 'address2 -> address2, 'salt -> salt,
          'ssn_1 -> ssn_1, 'ssn_2 -> ssn_2
        ).executeUpdate()
    }
  }

  def stsToggle(id: String) = {

    var stsRaw = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      stsRaw = SQL(
        s"""SELECT tbl_user.sts
           |FROM tbl_user
           |WHERE user_id = '$id'
         """.stripMargin
      ).as(parser.*)
    }
    
    val sts = if (stsRaw(0)("tbl_user.sts") == "I") "O" else "I"

    db.withConnection { implicit conn =>
      SQL(
        """
          UPDATE tbl_user SET
          sts = {sts}
          WHERE user_id = {id}
      """
      )
        .on(
          'id -> id, 'sts -> sts
        ).executeUpdate()
    }

    var stsNew = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      stsNew = SQL(
        s"""SELECT tbl_user.sts
           |FROM tbl_user
           |WHERE user_id = '$id'
         """.stripMargin
      ).as(parser.*)
    }
    stsNew
  }

  def passwordReset(id:String) = {
    val salt = commonUtil.createSalt()
    val hashedPw = commonUtil.passwordHashing("0000" + salt)

    db.withConnection { implicit conn =>
      SQL(
        """
          UPDATE tbl_user SET
          user_pwd = {pw},
          user_salt = {salt}
          WHERE user_id = {id}
      """
      )
        .on(
          'id -> id, 'pw -> hashedPw, 'salt -> salt
        ).executeUpdate()
    }
  }

  def userDelete(id: String) = {
    db.withConnection { implicit conn =>
      SQL(
        """
          DELETE FROM tbl_user
          WHERE user_id = {id}
      """
      )
        .on(
          'id -> id
        ).executeUpdate()
    }
  }
}
