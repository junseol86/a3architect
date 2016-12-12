package models

import javax.inject._

import anorm.{RowParser, SqlParser, _}
import play.api.db._

/**
  * Created by Hyeonmin on 2016-12-05.
  */
class Promotion @Inject()(db: Database) {

  val parser: RowParser[Map[String, Any]] =
    SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
      Right(map + (meta.column.qualified -> value))
    }

  val pageSize = 2

  def getPromotions(category: String, page: Int) = {
    val pageOffset = (page - 1) * pageSize

    var list = List[Map[String, Any]]()
    var count = List[Map[String, Any]]()

    val commonQuery =
      f"""FROM tbl_promotion
         WHERE prom_category LIKE "%%$category%s%%"
         """
    val listQuery =
      f"""SELECT prom_idx, prom_category, prom_title, prom_subtitle, prom_thumbnail, prom_modified
         $commonQuery%s
         ORDER BY prom_idx DESC
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

  def getAPromotion(idx: String) = {
    var result = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      result = SQL(
        s"""SELECT * FROM tbl_promotion
           WHERE prom_idx = '$idx'
           """.stripMargin).as(parser.*)
    }
    result(0)
  }

  def contractStoryWrite(
                        category:String, title: String, subtitle: String, thumbnail: String, content:String, created:String
                        ) = {
    db.withConnection { implicit conn =>
      SQL(
        """
          INSERT INTO tbl_promotion (
          prom_category, prom_title, prom_subtitle, prom_thumbnail, prom_content, prom_created, prom_modified
          ) values (
          {category}, {title}, {subtitle}, {thumbnail}, {content}, {created}, {modified}
          )
      """
      )
        .on(
          'category -> category, 'title -> title, 'content -> content, 'created -> created, 'modified -> created
        ).executeInsert()
    }
  }

  def contractStoryModify(
                          idx:String, category:String, title: String, subtitle: String, thumbnail: String, content:String, modified:String
                        ) = {
    db.withConnection { implicit conn =>
      SQL(
        """
          UPDATE tbl_promotion SET
          prom_category = {category}, prom_title = {title}, prom_subtitle = {subtitle}, prom_thumbnail = {thumbnail},
          prom_content = {content}, prom_modified = {modified}
          WHERE prom_idx = {idx}
      """
      )
        .on(
          'idx -> idx, 'category -> category, 'title -> title, 'subtitle -> subtitle, 'thumbnail -> thumbnail, 'content -> content, 'modified -> modified
        ).executeUpdate()
    }
  }
}
