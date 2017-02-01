package models

import javax.inject._

import anorm.{RowParser, SqlParser, _}
import play.api.db._

/**
  * Created by Hyeonmin on 2016-12-05.
  */
class News @Inject()(db: Database) {

  val parser: RowParser[Map[String, Any]] =
    SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
      Right(map + (meta.column.qualified -> value))
    }

  val pageSize = 15

  def getDashboardNewses() = {
    var list = List[Map[String, Any]]()
    val query = "SELECT * FROM tbl_news ORDER BY news_idx DESC LIMIT 3"
    db.withConnection{implicit conn =>
      list = SQL(query.stripMargin).as(parser.*)
    }
    list
  }

  def getNewses(search: String, page: Int) = {
    val pageOffset = (page - 1) * pageSize

    var list = List[Map[String, Any]]()
    var count = List[Map[String, Any]]()

    val commonQuery =
      f"""FROM tbl_news
         WHERE (
         news_title LIKE "%%$search%s%%"
         OR news_content LIKE "%%$search%s%%"
         )"""
    val listQuery =
      f"""SELECT news_idx, news_title, news_modified
         $commonQuery%s
         ORDER BY news_idx DESC
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

  def getANews(idx: String) = {
    var result = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      result = SQL(
        s"""SELECT * FROM tbl_news
           WHERE news_idx = '$idx'
           """.stripMargin).as(parser.*)
    }
    result(0)
  }

  def newsWrite(
                        title: String, content:String, created:String
                        ) = {
    db.withConnection { implicit conn =>
      SQL(
        """
          INSERT INTO tbl_news (
          news_title, news_content, news_created, news_modified
          ) values (
          {title}, {content}, {created}, {modified}
          )
      """
      )
        .on(
          'title -> title, 'content -> content, 'created -> created, 'modified -> created
        ).executeInsert()
    }
  }

  def newsModify(
                          idx:String, title: String, content:String, modified:String
                        ) = {
    db.withConnection { implicit conn =>
      SQL(
        """
          UPDATE tbl_news SET
          news_title = {title}, news_content = {content}, news_modified = {modified}
          WHERE news_idx = {idx}
      """
      )
        .on(
          'idx -> idx, 'title -> title, 'content -> content, 'modified -> modified
        ).executeUpdate()
    }
  }
}
