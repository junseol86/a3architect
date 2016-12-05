package models

import javax.inject._

import anorm.{RowParser, SqlParser, _}
import play.api.db._

/**
  * Created by Hyeonmin on 2016-12-05.
  */
class ContractStory @Inject()(db: Database) {

  val parser: RowParser[Map[String, Any]] =
    SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
      Right(map + (meta.column.qualified -> value))
    }

  def contractStoryWrite(
                        category:String, title: String, content:String, created:String, modified:String
                        ) = {
    db.withConnection { implicit conn =>
      SQL(
        """
          INSERT INTO contract_story (
          cs_category, cs_title, cs_content, cs_created, cs_modified
          ) values (
          {category}, {title}, {content}, {created}, {modified}
          )
      """
      )
        .on(
          'category -> category, 'title -> title, 'content -> content, 'created -> created, 'modified -> modified
        ).executeInsert()
    }
  }
}
