package models

import javax.inject._

import anorm.{RowParser, SqlParser, _}
import play.api.db._

/**
  * Created by Hyeonmin on 2017-01-10.
  */
class Project @Inject()(db: Database) {

  val parser: RowParser[Map[String, Any]] =
    SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
      Right(map + (meta.column.qualified -> value))
    }

  val pageSize = 2

  def getOptions = {
    var options = Map[String, List[Map[String, Any]]]()
    var yongdos = List[Map[String, Any]]()
    var gujos = List[Map[String, Any]]()
    var styles = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      yongdos = SQL(
        """
          |SELECT DISTINCT yongdo_main FROM tbl_project_yongdo
          |""".stripMargin).as(parser.*)
      gujos = SQL(
        """
          |SELECT DISTINCT gujo_main FROM tbl_project_gujo
          |""".stripMargin).as(parser.*)
    }
    options += "yongdos" -> yongdos
    options += "gujos" -> gujos
    options
  }

  def getYears = {
    var years = List[Map[String, Any]]()
    db.withConnection {implicit  conn =>
      years = SQL(
        """SELECT DISTINCT pj_year FROM tbl_project""".stripMargin
      ).as(parser.*)
      years
    }

  }

  def getProjects(page: Int) = {
    val pageOffset = (page - 1) * pageSize

    var list = List[Map[String, Any]]()
    var count = List[Map[String, Any]]()

    val commonQuery =
      f"""FROM tbl_project"""
    val listQuery =
      f"""SELECT pj_idx, pj_title, pj_modified
         $commonQuery%s
         ORDER BY pj_idx DESC
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
  
  def getAProject(idx: String) = {
    var result = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      result = SQL(
        s"""SELECT * FROM tbl_project
           WHERE pj_idx = '$idx'
           """.stripMargin).as(parser.*)
    }
    result(0)
  }
  
  def projectChildren(idx: String) = {
    var construction = List[Map[String, Any]]()
    var design = List[Map[String, Any]]()
    var interior = List[Map[String, Any]]()
    var sites = List[Map[String, Any]]()
    
    db.withConnection{implicit conn =>
      construction = SQL(
        f"""
            SELECT COUNT(*) AS total FROM view_portfolio WHERE pj_idx = $idx%s AND pf_category = "construction"
         """.stripMargin).as(parser.*)
      design = SQL(
        f"""
            SELECT COUNT(*) AS total FROM view_portfolio WHERE pj_idx = $idx%s AND pf_category = "design"
         """.stripMargin).as(parser.*)
      interior = SQL(
        f"""
            SELECT COUNT(*) AS total FROM view_portfolio WHERE pj_idx = $idx%s AND pf_category = "interior"
         """.stripMargin).as(parser.*)
      sites = SQL(
        f"""
            SELECT COUNT(*) AS total FROM view_sites WHERE pj_idx = $idx%s AND sts_idx != ""
         """.stripMargin).as(parser.*)
    }

    (construction(0)(".total"), design(0)(".total"), interior(0)(".total"), sites(0)(".total"))
    
  }

  def projectWrite(
                 title: String, subtitle: String, clientName: String, clientId: String, yongdo: String, gujo: String, jaeryo: String, year: String, location: String, daeji: String, gunchuk: String, yeon: String, gyumo: String, state: String, created:String, hashtag: String
               ) = {
    db.withConnection { implicit conn =>
      SQL(
        """
          INSERT INTO tbl_project (
          pj_title, pj_subtitle, pj_client_name, pj_client_id,
          pj_yongdo, pj_gujo, pj_jaeryo, pj_year, pj_location,
          pj_daeji, pj_gunchuk, pj_yeon,
          pj_gyumo, pj_state,
          pj_created, pj_modified,
          pj_hashtag
          ) values (
          {title}, {subtitle}, {client_name}, {client_id},
          {yongdo}, {gujo}, {jaeryo}, {year}, {location},
          {daeji}, {gunchuk}, {yeon},
          {gyumo}, {state},
          {created}, {modified},
          {hashtag}
          )
      """
      )
        .on(
          'title -> title, 'subtitle -> subtitle, 'client_name -> clientName, 'client_id -> clientId,
          'yongdo -> yongdo, 'gujo -> gujo, 'jaeryo -> jaeryo, 'year -> year, 'location -> location,
          'daeji -> daeji, 'gunchuk -> gunchuk, 'yeon -> yeon,
          'gyumo -> gyumo, 'state -> state,
          'created -> created, 'modified -> created,
          'hashtag -> (hashtag + " ")
        ).executeInsert()
    }
  }

  def projectModify(
                  idx:String,
                  title: String, subtitle: String, clientName: String, clientId: String, yongdo: String, gujo: String, jaeryo: String, year: String, location: String, daeji: String, gunchuk: String, yeon: String, gyumo: String, state: String, created:String, hashtag: String
                ) = {
    db.withConnection { implicit conn =>
      SQL(
        """
          UPDATE tbl_project SET
          pj_title = {title}, pj_subtitle = {subtitle}, pj_client_name = {client_name}, pj_client_id = {client_id},
          pj_yongdo = {yongdo}, pj_gujo = {gujo}, pj_jaeryo = {jaeryo}, pj_year = {year}, pj_location = {location},
          pj_daeji = {daeji}, pj_gunchuk = {gunchuk}, pj_yeon = {yeon},
          pj_gyumo = {gyumo}, pj_state = {state},
          pj_created = {created}, pj_modified = {modified},
          pj_hashtag = {hashtag}
          WHERE pj_idx = {idx}
      """
      )
        .on(
          'idx -> idx,
          'title -> title, 'subtitle -> subtitle, 'client_name -> clientName, 'client_id -> clientId,
          'yongdo -> yongdo, 'gujo -> gujo, 'jaeryo -> jaeryo, 'year -> year, 'location -> location,
          'daeji -> daeji, 'gunchuk -> gunchuk, 'yeon -> yeon,
          'gyumo -> gyumo, 'state -> state,
          'created -> created, 'modified -> created,
          'hashtag -> (hashtag + " ")
        ).executeUpdate()
    }
  }

}
