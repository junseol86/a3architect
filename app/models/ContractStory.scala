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

  val pageSize = 1

  def getContractStories(category: Int, search: String, page: Int) = {
    val pageOffset = (page - 1) * pageSize

    var list = List[Map[String, Any]]()
    var count = List[Map[String, Any]]()

    val commonQuery =
      f"""FROM tbl_contract_story
         WHERE cs_category = $category%s
         AND (
         cs_title LIKE "%%$search%s%%"
         OR cs_content LIKE "%%$search%s%%"
         )"""
    val listQuery =
      f"""SELECT * $commonQuery%s
         ORDER BY cs_idx DESC
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

  def contractStoryWrite(
                        category:String, title: String, content:String, created:String, modified:String
                        ) = {
    db.withConnection { implicit conn =>
      SQL(
        """
          INSERT INTO tbl_contract_story (
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
