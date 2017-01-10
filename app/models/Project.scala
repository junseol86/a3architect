package models

import javax.inject._

import anorm.{RowParser, SqlParser, _}
import play.api.db._

/**
  * Created by Hyeonmin on 2017-01-10.
  */
class Project @Inject()(db: Database) {

  val parser: RowParser[Map[String, Any]] =
    SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
      Right(map + (meta.column.qualified -> value))
    }

  val pageSize = 2

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

  def getProjects(page: Int) = {
    val pageOffset = (page - 1) * pageSize

    var list = List[Map[String, Any]]()
    var count = List[Map[String, Any]]()

    val commonQuery =
      f"""FROM tbl_project"""
    val listQuery =
      f"""SELECT pj_idx, pj_title, pj_modified
         $commonQuery%s
         ORDER BY pj_idx DESC
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
  
  def getAProject(idx: String) = {
    var result = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      result = SQL(
        s"""SELECT * FROM tbl_project
           WHERE pj_idx = '$idx'
           """.stripMargin).as(parser.*)
    }
    result(0)
  }

}
