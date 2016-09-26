package controllers.admin

import scala.concurrent.Future
import scala.concurrent.Future.{successful => future}

import controllers.auth.AuthConfigAdminImpl
import forms._
import javax.inject.Inject
import jp.t2v.lab.play2.auth.AuthActionBuilders
import models.TablesExtend._
import play.api._
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._
import play.api.mvc.Action
import play.api.mvc.Controller
import play.filters.csrf._
import play.filters.csrf.CSRF.Token
import services.UserAccountServiceLike
import services.dao._
import skinny.util.JSONStringOps._
import views._
import utilities._
import utilities.auth.Role
import utilities.auth.Role._
import java.util.Calendar


class LoginHistoryController @Inject()(addToken: CSRFAddToken, checkToken: CSRFCheck, val userAccountService: UserAccountServiceLike, loginHistoriesDao: LoginHistoriesDAO, val messagesApi: MessagesApi) extends Controller with AuthActionBuilders with AuthConfigAdminImpl with I18nSupport  {
  val UserAccountSv = userAccountService

  val pageOffset = PageNation.defaultMaxPageNum
  
  def show(page: Int = 1) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      val currentuser: User = request.user
      loginHistoriesDao.paginglistByAccountId(currentuser.id, page, pageOffset).map{case (pagecount,histories) =>
        Ok(views.html.loginhistory.list("", histories.toList, new PageNation(page, pagecount, pageOffset)))
      }
   }
  }
  def pagenation(currentPageNum: Int, pageName: String) = AuthorizationAction(NormalUser) { implicit request =>
     pageName match {
      case "index" => Redirect(controllers.admin.routes.LoginHistoryController.show(currentPageNum))
      case _       => Redirect(controllers.admin.routes.LoginHistoryController.show(currentPageNum))
    }
  }
}