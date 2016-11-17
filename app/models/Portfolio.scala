package models
import javax.inject._
import play.api.db._
import anorm._
import anorm.{ RowParser, SqlParser }

/**
  * Created by Hyeonmin on 2016-11-17.
  */
class Portfolio @Inject()(db: Database) {

  val parser: RowParser[Map[String, Any]] =
    SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
      Right(map + (meta.column.qualified -> value))
    }

  def getList = {
    var result = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      result = SQL(
        """
          |SELECT * FROM tbl_portfolio
          |ORDER BY idx DESC
          |""".stripMargin).as(parser.*)
    }
    result
  }
}
