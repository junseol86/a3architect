package models

import javax.inject._

import anorm.{RowParser, SqlParser, _}
import play.api.db._

/**
  * Created by Hyeonmin on 2016-12-05.
  */
class SpaceStory @Inject()(db: Database) {

  val parser: RowParser[Map[String, Any]] =
    SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
      Right(map + (meta.column.qualified -> value))
    }

  val pageSize = 9

  def getSpaceStories(category: String, page: Int) = {
    val pageOffset = (page - 1) * pageSize

    var list = List[Map[String, Any]]()
    var count = List[Map[String, Any]]()

    val commonQuery =
      f"""FROM tbl_space_story
         WHERE ss_category LIKE "%%$category%s%%"
         """
    val listQuery =
      f"""SELECT ss_idx, ss_category, ss_title, ss_thumbnail, ss_modified
         $commonQuery%s
         ORDER BY ss_idx DESC
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

  def getASpaceStory(idx: String) = {
    var result = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      result = SQL(
        s"""SELECT * FROM tbl_space_story
           WHERE ss_idx = '$idx'
           """.stripMargin).as(parser.*)
    }
    result(0)
  }

  def space_storyWrite(
                        category:String, title: String, thumbnail: String, content:String, created:String
                        ) = {
    db.withConnection { implicit conn =>
      SQL(
        """
          INSERT INTO tbl_space_story (
          ss_category, ss_title, ss_thumbnail, ss_content, ss_created, ss_modified
          ) values (
          {category}, {title}, {thumbnail}, {content}, {created}, {modified}
          )
      """
      )
        .on(
          'category -> category, 'title -> title, 'thumbnail -> thumbnail, 'content -> content, 'created -> created, 'modified -> created
        ).executeInsert()
    }
  }

  def space_storyModify(
                          idx:String, category:String, title: String, thumbnail: String, content:String, modified:String
                        ) = {
    db.withConnection { implicit conn =>
      SQL(
        """
          UPDATE tbl_space_story SET
          ss_category = {category}, ss_title = {title}, ss_thumbnail = {thumbnail},
          ss_content = {content}, ss_modified = {modified}
          WHERE ss_idx = {idx}
      """
      )
        .on(
          'idx -> idx, 'category -> category, 'title -> title, 'thumbnail -> thumbnail, 'content -> content, 'modified -> modified
        ).executeUpdate()
    }
  }
}
