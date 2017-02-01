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

  val pageSize = 15

  def getDashboardContractStories() = {
    var list = List[Map[String, Any]]()
    val query = "SELECT * FROM tbl_contract_story ORDER BY cs_idx DESC LIMIT 3"
    db.withConnection{implicit conn =>
      list = SQL(query.stripMargin).as(parser.*)
    }
    list
  }

  def getContractStories(category: String, search: String, page: Int) = {
    val pageOffset = (page - 1) * pageSize

    var list = List[Map[String, Any]]()
    var count = List[Map[String, Any]]()

    val commonQuery =
      f"""FROM tbl_contract_story
         WHERE cs_category LIKE "%%$category%s%%"
         AND (
         cs_title LIKE "%%$search%s%%"
         OR cs_content LIKE "%%$search%s%%"
         )"""
    val listQuery =
      f"""SELECT cs_idx, cs_category, cs_title, cs_modified
         $commonQuery%s
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

  def getAContractStory(idx: String) = {
    var result = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      result = SQL(
        s"""SELECT * FROM tbl_contract_story
           WHERE cs_idx = '$idx'
           """.stripMargin).as(parser.*)
    }
    result(0)
  }

  def contractStoryWrite(
                        category:String, title: String, content:String, created:String
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
          'category -> category, 'title -> title, 'content -> content, 'created -> created, 'modified -> created
        ).executeInsert()
    }
  }

  def contractStoryModify(
                          idx:String, category:String, title: String, content:String, modified:String
                        ) = {
    db.withConnection { implicit conn =>
      SQL(
        """
          UPDATE tbl_contract_story SET
          cs_category = {category}, cs_title = {title}, cs_content = {content}, cs_modified = {modified}
          WHERE cs_idx = {idx}
      """
      )
        .on(
          'idx -> idx, 'category -> category, 'title -> title, 'content -> content, 'modified -> modified
        ).executeUpdate()
    }
  }
}
