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
import utilities.auth.Role
import utilities.auth.Role._
import java.util.Calendar


class AccountController @Inject()(addToken: CSRFAddToken, checkToken: CSRFCheck, val userAccountService: UserAccountServiceLike, accountsDao: AccountsDAO, AccountsForm:AccountsForm, val messagesApi: MessagesApi) extends Controller with AuthActionBuilders with AuthConfigAdminImpl with I18nSupport  {
  val UserAccountSv = userAccountService

  
  /** This result directly redirect to the application home.*/
  val Home = Redirect(controllers.admin.routes.AccountController.index())

  
  def getToken = addToken(Action { implicit request =>
    val Token(name, value) = CSRF.getToken.get
    Ok(s"$name=$value")
  })
  
  def index = addToken{
    AuthorizationAction(Administrator).async { implicit request =>
      accountsDao.all().map(accounts => Ok(views.html.account.list("", accounts)))
    }
  }
  def add = addToken{
    AuthorizationAction(Administrator) {implicit request =>
      val currentuser: User = request.user
      Ok(views.html.account.regist("", AccountsForm.formnew, currentuser))
    }
  }
  
  def create = checkToken{
    AuthorizationAction(Administrator).async { implicit request =>
      val currentuser: User = request.user
      AccountsForm.formnew.bindFromRequest.fold(
          formWithErrors => {
            Logger.debug(formWithErrors.toString())
            Future(BadRequest(views.html.account.regist("エラー", formWithErrors, currentuser)))
          },
          account => {
            accountsDao.create(account).flatMap(cnt =>
                //if (cnt != 0) accountsDao.all().map(accounts => Ok(views.html.account.list("登録しました", accounts)))
                if (cnt != 0) Future.successful(Redirect(controllers.admin.routes.AccountController.index))
                else accountsDao.all().map(notifications => BadRequest(views.html.account.edit("エラー", AccountsForm.formnew.fill(account), currentuser)))
             )
          }
      )
    }
  }
   
  def edit(accountId: Int) = addToken{
    AuthorizationAction(Administrator).async {implicit request =>
      val currentuser: User = request.user
      accountsDao.findById(accountId).flatMap(option =>
        option match {
          case Some(account) => Future(Ok(views.html.account.edit("GET", AccountsForm.formedit.fill(account), currentuser)))
          case None => accountsDao.all().map(accounts => BadRequest(views.html.account.list("エラー", accounts)))
        }
      )
    }
  }

  def update = checkToken {
    AuthorizationAction(Administrator).async { implicit request =>
      val currentuser: User = request.user
      AccountsForm.formedit.bindFromRequest.fold(
          formWithErrors => {
            Future(BadRequest(views.html.account.edit("ERROR", formWithErrors, currentuser)))
          },
          account => {
            accountsDao.update_mappinged(account).flatMap(cnt =>
              if (cnt != 0) accountsDao.all().map(accounts => Ok(views.html.account.list("更新しました", accounts)))
              //if (cnt != 0) Future.successful(Redirect(routes.AccountController.index))
              else accountsDao.all().map(notifications => BadRequest(views.html.account.edit("エラー", AccountsForm.formedit.fill(account), currentuser)))
            )
          }
      )
    }
  }
  
  def delete(id: Int) = checkToken{
    AuthorizationAction(Administrator).async {implicit request =>
      accountsDao.delete(id).flatMap(cnt =>
        if (cnt != 0) accountsDao.all().map(accounts => Ok(views.html.account.list("削除しました", accounts)))
        else accountsDao.all().map(accounts => BadRequest(views.html.account.list("エラー", accounts)))
      )
    }
  }
    
  def selfedit = AuthorizationAction(NormalUser) { implicit request =>
    val currentuser: User = request.user
    Redirect(controllers.admin.routes.AccountController.edit(currentuser.id))
  }
}