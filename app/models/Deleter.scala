package models

import javax.inject._

import anorm.{RowParser, SqlParser, _}
import play.api.db._

class Deleter @Inject()(db: Database) {
  val parser: RowParser[Map[String, Any]] =
    SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
      Right(map + (meta.column.qualified -> value))
    }

  def deleteAContent(table: String, column: String, idx: String): Int = {
    db.withConnection { implicit conn =>
      SQL(
        s"DELETE FROM $table WHERE $column = {idx}"
      )
        .on(
          'column -> column, 'idx -> idx
        ).executeUpdate()
    }
  }

}
