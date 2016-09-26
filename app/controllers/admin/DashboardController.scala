package controllers.admin

import scala.concurrent.Future
import scala.concurrent.Future.{successful => future}

import javax.inject._
import play.api._
import play.api.mvc._
import controllers.auth.AuthConfigAdminImpl
import jp.t2v.lab.play2.auth.AuthActionBuilders
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import play.api.mvc.Action
import play.api.mvc.Controller
import play.filters.csrf._
import play.filters.csrf.CSRF.Token
import services.UserAccountServiceLike
import views._
import services.dao._
import utilities._
import utilities.config._
import utilities.auth.Role
import utilities.auth.Role._

import java.util.Date

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

class DashboardController @Inject()(addToken: CSRFAddToken, checkToken: CSRFCheck, val userAccountService: UserAccountServiceLike, loginHistoriesDao: LoginHistoriesDAO, val messagesApi: MessagesApi)  extends Controller with AuthActionBuilders with AuthConfigAdminImpl with I18nSupport {
  val UserAccountSv = userAccountService


  def index = addToken{
    AuthorizationAction(Administrator) { implicit request =>
      loginHistoriesDao.rotateOld(DateHelper.getDateBeforeMonth(new Date, LoginHistoriesConfig.borderMonthTerm))
      Ok(views.html.dashboard.index("dashboad"))
    }
  }


}
