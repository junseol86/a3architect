package models
import javax.inject._
import play.api.db._
import anorm._
import anorm.{ RowParser, SqlParser }

/**
  * Created by Hyeonmin on 2017-01-16.
  */
class Sites @Inject()(db: Database) {

  val parser: RowParser[Map[String, Any]] =
    SqlParser.folder(Map.empty[String, Any]) { (map, value, meta) =>
      Right(map + (meta.column.qualified -> value))
    }

  val pageSize = 5

  def getASites(idx: String) = {
    var result = List[Map[String, Any]]()
    db.withConnection{implicit conn =>
      result = SQL(
        f"""SELECT * FROM tbl_sites
           WHERE sts_pj_idx = '$idx'
           """.stripMargin).as(parser.*)
    }
    result(0)
  }

  def sitesWrite(
                      pj_idx: String, thumbnail:String, created:String, category: String, inCharge: String, inChargeFrom: String, inChargePhoto: String
                    ) = {
    db.withConnection { implicit conn =>
      SQL(
        """
          INSERT INTO tbl_sites (
          sts_pj_idx, sts_thumbnail, sts_created, sts_modified, sts_category, sts_in_charge, sts_in_charge_from, sts_in_charge_photo
          ) values (
          {pj_idx}, {thumbnail}, {created}, {modified}, {category}, {in_charge}, {in_charge_from}, {in_charge_photo}
          )
      """
      )
        .on(
          'pj_idx -> pj_idx, 'thumbnail -> thumbnail, 'created -> created, 'modified -> created, 'category -> category, 'in_charge -> inCharge, 'in_charge_from -> inChargeFrom, 'in_charge_photo -> inChargePhoto
        ).executeInsert()
    }
  }

  def sitesModify(
                       idx: String,
                       thumbnail:String, created:String, category: String, inCharge: String, inChargeFrom: String, inChargePhoto: String
                     ) = {
    db.withConnection { implicit conn =>
      SQL(
        """
          UPDATE tbl_sites SET
          sts_thumbnail = {thumbnail}, sts_modified = {modified}, sts_category = {category}, sts_in_charge = {in_charge}, sts_in_charge_from = {in_charge_from}, sts_in_charge_photo = {in_charge_photo}
          WHERE sts_idx = {idx}
      """
      )
        .on(
          'idx -> idx, 'thumbnail -> thumbnail, 'modified -> created, 'category -> category, 'in_charge -> inCharge, 'in_charge_from -> inChargeFrom, 'in_charge_photo -> inChargePhoto
        ).executeUpdate()
    }
  }

  def getSitesScenes(pj_idx: String) = {
    val categorizedList = List[Map[String, Any]]()
    var list = List[Map[String, Any]]()
    var categories = List[Map[String, Any]]()
    db.withConnection { implicit  conn =>
      list = SQL(
        f"""SELECT * FROM tbl_sites_pic WHERE stsp_pj_idx = "$pj_idx%s"""".stripMargin
      ).as(parser *)

      categories = SQL(
        f"""SELECT DISTINCT(stsp_category) FROM tbl_sites_pic WHERE stsp_pj_idx = "$pj_idx%s"""".stripMargin
      ).as(parser *)
    }

    def makeCategorizedList(categoriesArg: List[Map[String, Any]], listArg:List[Map[String, Any]], categorizedListArg:List[Map[String, Any]]):List[Map[String, Any]] = {
      if (categoriesArg.length == 0) categorizedListArg
      else {
        val category = categoriesArg.head("tbl_sites_pic.stsp_category").toString
        val filtered = listArg.filter((item: Map[String, Any]) => item("tbl_sites_pic.stsp_category").toString == category)
        var intoUpdated = Map[String, Any]()
        intoUpdated += "category" -> category
        intoUpdated += "scenes" -> filtered
        makeCategorizedList(categoriesArg.tail, listArg,  categorizedListArg :+ intoUpdated)
      }
    }

    makeCategorizedList(categories, list, categorizedList)
  }

  def sitesSceneWrite(pj_idx: String, stsp_category: String, stsp_title: String,  url: String) = {
    println(pj_idx)
    println(stsp_category)
    println(stsp_title)
    println(url)
    db.withConnection { implicit conn =>
      SQL(
        """
            INSERT INTO tbl_sites_pic (
            stsp_pj_idx, stsp_category, stsp_title, stsp_url
            ) VALUES (
            {pj_idx}, {stsp_category}, {stsp_title}, {url}
            )
          """).on('pj_idx -> pj_idx, 'stsp_category -> stsp_category, 'stsp_title -> stsp_title, 'url -> url).executeInsert()
    }
  }

}
