package models

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.db._
import anorm._
import anorm.{ RowParser, SqlParser }

class User @Inject()(db: Database) {

  val parser: RowParser[Map[String, Any]] =
    SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
      Right(map + (meta.column.qualified -> value))
    }

  def getList = {
    var result = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      result = SQL("""SELECT * FROM tbl_user""").as(parser.*)
    }
    result
  }
}
