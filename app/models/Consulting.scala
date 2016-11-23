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

  def getOptions = {
    var options = Map[String, List[Map[String, Any]]]()
    var yongdos = List[Map[String, Any]]()
    var gujos = List[Map[String, Any]]()
    var styles = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      yongdos = SQL(
        """
          |SELECT * FROM tbl_consult_yongdo
          |""".stripMargin).as(parser.*)
      gujos = SQL(
        """
          |SELECT * FROM tbl_consult_gujo
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
}
