package controllers._A_admin

import javax.inject._

import controllers.LoginSession
import models.{Portfolio, Deleter, User}
import play.api.mvc._
import utils.CommonUtil

/**
  * Created by Hyeonmin on 2017-01-11.
  */
class AdminPortfolioCtrl @Inject()(user: User, loginSession: LoginSession,
                                   portfolioMdl: Portfolio, deleter: Deleter,
                                   commonUtil: CommonUtil) extends Controller {

}
