package controllers.auth

import javax.inject.Inject

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import java.util.Date
import javax.inject.Inject
import services._
import services.dao._
import models.TablesExtend._
import utilities._
import forms._
import views._
import play.api._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._
import play.api.mvc.Action
import play.api.mvc.Controller
import skinny.util.JSONStringOps._
import java.sql.Timestamp
import org.joda.time.DateTime
import org.joda.time.format._
import play.api.i18n.{MessagesApi, I18nSupport}

import play.filters.csrf._
import play.filters.csrf.CSRF.Token

import jp.t2v.lab.play2.auth.LoginLogout
import utilities.config.AppSelectorConfig

import play.api.cache._




class LoginLogoutController @Inject()(addToken: CSRFAddToken, checkToken: CSRFCheck, cache: CacheApi, val userAccountService: UserAccountServiceLike, loginHistoriesDao: LoginHistoriesDAO, LoginForm:LoginForm, val messagesApi: MessagesApi)
  extends Controller with LoginLogout with AuthConfigImpl with I18nSupport{

  val UserAccountSv = userAccountService
  
  def index = addToken{
    Action { implicit request =>
      Ok(views.html.auth.login(LoginForm.loginForm))
    }
  }

  def login() = checkToken {
    Action.async { implicit request =>
      LoginForm.loginForm.bindFromRequest.fold(
        formWithErrors => {
           Future.successful(BadRequest(views.html.auth.login(formWithErrors)))
        },
        account => {
          userAccountService.authenticate(account).flatMap {
            user => user match {
              case Some(user) =>
                val req = request.copy(tags = request.tags + ("rememberme" -> LoginForm.loginForm.bindFromRequest.get.toString))
                createLoginHistory(user.id, account.loginId, request.headers.get("User-Agent").getOrElse(""), request.remoteAddress, true)
                gotoLoginSucceeded(user.id)(req, defaultContext).map(_.withSession(request.session +("rememberme" -> LoginForm.loginForm.bindFromRequest.get.toString)))
              case _ =>
                createLoginHistory(-1, account.loginId, request.headers.get("User-Agent").getOrElse(""), request.remoteAddress, false)
                Future.successful(Unauthorized(views.html.auth.login(LoginForm.loginForm.fill(account))))
            }
          }
        }
      )
    }
  }

  def logout() = Action.async { implicit request =>
      cache.remove(AppSelectorConfig.AppSelectorCacheKey + "-" + request.session.get("uuid"))
      gotoLogoutSucceeded
    }
  def createLoginHistory(userid: Int = -1, input_login_id: String, useragent: String, remoteaddress: String ,islogined: Boolean) = {
    val loginhistory = new LoginHistoriesRow(0,Option(userid), Option(input_login_id), Option(useragent), Option(remoteaddress), Option(islogined), new Date);
    loginHistoriesDao.create(loginhistory)
  }
}

