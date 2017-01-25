package models

import javax.inject._

import anorm.{RowParser, SqlParser, _}
import play.api.db._

/**
  * Created by Hyeonmin on 2016-11-23.
  */
class Consulting @Inject()(db: Database) {

  val parser: RowParser[Map[String, Any]] =
    SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
      Right(map + (meta.column.qualified -> value))
    }
  
  val pageSize = 15

  def getConApplies(category: String, page: Int) = {
    val pageOffset = (page - 1) * pageSize

    var list = List[Map[String, Any]]()
    var count = List[Map[String, Any]]()

    val commonQuery =
      f"""FROM tbl_con_apply
         WHERE ca_category LIKE "%%$category%s%%"
         """
    val listQuery =
      f"""SELECT *
         $commonQuery%s
         ORDER BY ca_idx DESC
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

  def getAConApply(idx: String) = {
    var result = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      result = SQL(
        s"""SELECT * FROM tbl_con_apply
           WHERE ca_idx = '$idx'
           """.stripMargin).as(parser.*)
    }
    result(0)
  }

  def getAsApplies(page: Int) = {
    val pageOffset = (page - 1) * pageSize

    var list = List[Map[String, Any]]()
    var count = List[Map[String, Any]]()

    val commonQuery =
      f"""FROM tbl_as_apply
         """
    val listQuery =
      f"""SELECT *
         $commonQuery%s
         ORDER BY aa_idx DESC
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

  def getAAsApply(idx: String) = {
    var result = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      result = SQL(
        s"""SELECT * FROM tbl_as_apply
           WHERE aa_idx = '$idx'
           """.stripMargin).as(parser.*)
    }
    result(0)
  }


  def getOptions = {
    var options = Map[String, List[Map[String, Any]]]()
    var yongdos = List[Map[String, Any]]()
    var gujos = List[Map[String, Any]]()
    var styles = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      yongdos = SQL(
        """
          |SELECT * FROM tbl_project_yongdo
          |""".stripMargin).as(parser.*)
      gujos = SQL(
        """
          |SELECT * FROM tbl_project_gujo
          |""".stripMargin).as(parser.*)
      styles = SQL(
        """
          |SELECT * FROM tbl_consult_style
          |""".stripMargin).as(parser.*)
    }
    options += "yongdos" -> yongdos
    options += "gujos" -> gujos
    options += "styles" -> styles
    options
  }

  def con_apply(
                 category:String,
                 client_id: String, client_name: String, client_phone: String, client_email: String,
                 address_1: String, address_2: String, address_3: String,
                 purpose:String, pyung: String, floor: String, base_floor: String,
                 yongdo_main: String, yongdo_sub: String, gujo_main: String, gujo_sub: String, style: String,
                 meeting_from: String, meeting_to: String, meeting_hour: String,
                 budget: String, construction_date: String, others: String,
                 created: String
               ) = {
    db.withConnection{implicit conn =>
      SQL(
           """
          INSERT INTO tbl_con_apply (
          ca_category,
          ca_client_id, ca_client_name, ca_client_phone, ca_client_email,
          ca_address_1, ca_address_2, ca_address_3,
          ca_purpose, ca_pyung, ca_floor, ca_base_floor,
          ca_yongdo_main, ca_yongdo_sub, ca_gujo_main, ca_gujo_sub, ca_style,
          ca_meeting_from, ca_meeting_to, ca_meeting_hour,
          ca_budget, ca_construction_date, ca_others,
          ca_created
          ) values (
          {category},
          {client_id}, {client_name}, {client_phone}, {client_email},
          {address_1}, {address_2}, {address_3},
          {purpose}, {pyung}, {floor}, {base_floor},
          {yongdo_main}, {yongdo_sub}, {gujo_main}, {gujo_sub}, {style},
          {meeting_from}, {meeting_to}, {meeting_hour},
          {budget}, {construction_date}, {others},
          {created}
          )
      """
      )
        .on(
          'category -> category,
          'client_id -> client_id, 'client_name -> client_name, 'client_phone -> client_phone, 'client_email -> client_email,
          'address_1 -> address_1, 'address_2 -> address_2, 'address_3 -> address_3,
          'purpose -> purpose, 'pyung -> pyung, 'floor -> floor, 'base_floor -> base_floor,
          'yongdo_main -> yongdo_main, 'yongdo_sub -> yongdo_sub, 'gujo_main -> gujo_main, 'gujo_sub -> gujo_sub, 'style -> style,
          'meeting_from -> meeting_from, 'meeting_to -> meeting_to, 'meeting_hour -> meeting_hour,
          'budget -> budget, 'construction_date -> construction_date, 'others -> others,
          'created -> created
        ).executeInsert()
    }
  }

  def as_apply(
                 client_id: String, client_name: String, client_phone: String,
                 project_name: String,
                 address_1: String, address_2: String, address_3: String,
                 content: String,
                 created: String
               ) = {
    db.withConnection{implicit conn =>
      SQL(
        """
          INSERT INTO tbl_as_apply (
          aa_client_id, aa_client_name, aa_client_phone,
          aa_project_name,
          aa_address_1, aa_address_2, aa_address_3,
          aa_content,
          aa_created
          ) values (
          {client_id}, {client_name}, {client_phone},
          {project_name},
          {address_1}, {address_2}, {address_3},
          {content},
          {created}
          )
      """
      )
        .on(
          'client_id -> client_id, 'client_name -> client_name, 'client_phone -> client_phone,
          'project_name -> project_name,
          'address_1 -> address_1, 'address_2 -> address_2, 'address_3 -> address_3,
          'content -> content,
          'created -> created
        ).executeInsert()
    }
  }

}
