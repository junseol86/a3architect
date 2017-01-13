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

  val pageSize = 5

  def getAPortfolio(idx: String, category: String) = {
    var result = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      result = SQL(
        f"""SELECT * FROM tbl_portfolio
           WHERE pf_pj_idx = '$idx'
           AND pf_category = '$category%s'
           """.stripMargin).as(parser.*)
    }
    result(0)
  }

  def getPortfolios(category: String, hashtag: String, page: Int, yongdo: String, year: String, gujo: String,  gyumo_min: String, gyumo_max: String) = {
    val pageOffset = (page - 1) * pageSize

    var list = List[Map[String, Any]]()
    var count = List[Map[String, Any]]()

    val commonQuery =
      f"""FROM view_portfolio
         WHERE (
         pf_category = "$category%s"
         AND (pj_title LIKE "%%$hashtag%s%%"
         OR pj_subtitle LIKE "%%$hashtag%s%%"
         OR pj_hashtag LIKE "%%$hashtag%s%%")
         AND pj_yongdo LIKE "%%$yongdo%s%%"
         AND pj_gujo LIKE  "%%$gujo%s%%"
         AND pj_year LIKE "%%$year%s%%"
         AND pj_gyumo > $gyumo_min%s
         AND pj_gyumo <= $gyumo_max%s
         )"""
    val listQuery =
      f"""SELECT *
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

  def portfolioWrite(
                 pj_idx: String, content:String, thumbnail:String, created:String, category: String, inCharge: String, inChargeFrom: String, inChargePhoto: String
               ) = {
    db.withConnection { implicit conn =>
      SQL(
        """
          INSERT INTO tbl_portfolio (
          pf_pj_idx, pf_thumbnail, pf_content, pf_created, pf_modified, pf_category, pf_in_charge, pf_in_charge_from, pf_in_charge_photo
          ) values (
          {pj_idx}, {thumbnail}, {content}, {created}, {modified}, {category}, {in_charge}, {in_charge_from}, {in_charge_photo}
          )
      """
      )
        .on(
          'pj_idx -> pj_idx, 'content -> content, 'thumbnail -> thumbnail, 'created -> created, 'modified -> created, 'category -> category, 'in_charge -> inCharge, 'in_charge_from -> inChargeFrom, 'in_charge_photo -> inChargePhoto
        ).executeInsert()
    }
  }

  def portfolioModify(
                     idx: String,
                       content:String, thumbnail:String, created:String, inCharge: String, inChargeFrom: String, inChargePhoto: String
                ) = {
    db.withConnection { implicit conn =>
      SQL(
        """
          UPDATE tbl_portfolio SET
          pf_content = {content}, pf_thumbnail = {thumbnail}, pf_modified = {modified}, pf_in_charge = {in_charge}, pf_in_charge_from = {in_charge_from}, pf_in_charge_photo = {in_charge_photo}
          WHERE pf_idx = {idx}
      """
      )
        .on(
          'idx -> idx, 'content -> content, 'thumbnail -> thumbnail, 'modified -> created, 'in_charge -> inCharge, 'in_charge_from -> inChargeFrom, 'in_charge_photo -> inChargePhoto
        ).executeUpdate()
    }
  }

  def getPortfolioPics(pj_idx: String, pf_category: String) = {
    var list = List[Map[String, Any]]()
    db.withConnection { implicit  conn =>
      list = SQL(
        f"""SELECT * FROM tbl_portfolio_pic WHERE pfp_pj_idx = "$pj_idx%s" AND pfp_pf_category = "$pf_category%s" """.stripMargin
      ).as(parser *)
    }
    list
  }

  def portfolioPicsWrite(pj_idx: String, pf_category: String, urls: String) = {
    val urlArray = urls.tail.split("#")
    for (url <- urlArray) {
      db.withConnection { implicit  conn =>
        SQL(
          """
            INSERT INTO tbl_portfolio_pic (
            pfp_pj_idx, pfp_pf_category, pfp_url
            ) VALUES (
            {pj_idx}, {pf_category}, {url}
            )
          """).on('pj_idx -> pj_idx, 'pf_category -> pf_category, 'url -> url).executeInsert()
      }
    }
    "success"
  }


}
