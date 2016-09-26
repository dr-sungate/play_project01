package controllers.admin

import scala.concurrent.Future
import scala.concurrent.Future.{successful => future}

import jp.t2v.lab.play2.auth.AuthElement
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
import play.api.cache._
import play.filters.csrf._
import play.filters.csrf.CSRF.Token
import services.UserAccountServiceLike
import services.dao._
import skinny.util.JSONStringOps._
import views._
import utilities.auth.Role
import utilities.auth.Role._
import utilities.config._
import utilities._

import play.api.libs.json._
import jp.t2v.lab.play2.auth.AuthElement


import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration

class DefaultProductController @Inject()(addToken: CSRFAddToken, checkToken: CSRFCheck, cache: CacheApi, cached: Cached, val userAccountService: UserAccountServiceLike, defaultProductsDAO: DefaultProductsDAO, clientsDao: ClientsDAO, agenciesDao: AgenciesDAO, applicationsDao: ApplicationsDAO, DefaultProductsForm:DefaultProductsForm, val messagesApi: MessagesApi) extends Controller with AuthActionBuilders with AuthElement with AuthConfigAdminImpl with I18nSupport  {
  
  val UserAccountSv = userAccountService
  
  val pageOffset = PageNation.defaultMaxPageNum
  
  
 
  def add(applicationId: Long, agencyId: Long) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
 
        Future(Ok(views.html.defaultproduct.regist("", applicationId, agencyId, DefaultProductsForm.form, currentuser)))
      }
    }
  }
  
  def create(applicationId: Long, agencyId: Long) = checkToken{
    AuthorizationAction(NormalUser).async { implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        DefaultProductsForm.form.bindFromRequest.fold(
            formWithErrors => {
              Logger.debug(formWithErrors.toString())
              Future(BadRequest(views.html.defaultproduct.regist("エラー", applicationId, agencyId, formWithErrors, currentuser)))
             },
            defaultproducts => {
              defaultProductsDAO.create(defaultproducts).flatMap(cnt =>
                  //if (cnt != 0) agenciesDao.all().map(agencies => Ok(views.html.agency.list("登録しました", agencies)))
                  if (cnt != 0) {
                    Future.successful(Redirect(controllers.admin.routes.AgencyController.view(applicationId, agencyId)))
                  }else{
                    Future(BadRequest(views.html.defaultproduct.regist("エラー", applicationId, agencyId, DefaultProductsForm.form.fill(defaultproducts), currentuser)))
                  }
               )
            }
        )
      }
    }
  }
   

  def edit(applicationId: Long, agencyId: Long, defaultproductId: Long) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        val defaultproductOptions = for {
            defaultproduct <- defaultProductsDAO.findById(defaultproductId)
        } yield (defaultproduct)
        defaultproductOptions.map { case (defaultproduct) =>
          defaultproduct match {
            case Some(defaultproduct) => Ok(views.html.defaultproduct.edit("GET", applicationId, agencyId, DefaultProductsForm.form.fill(defaultproduct), currentuser))
            case _ => (Status(404)(views.html.errors.error404notfound("no found")))
          }
        }
      }
    }
  }

  def update(applicationId: Long, agencyId: Long) = checkToken {
    AuthorizationAction(NormalUser).async { implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        DefaultProductsForm.form.bindFromRequest.fold(
            formWithErrors => {
                  Future(BadRequest(views.html.defaultproduct.edit("エラー", applicationId, agencyId, formWithErrors, currentuser)))
            },
            defaultproduct => {
              defaultProductsDAO.update_mappinged(defaultproduct).flatMap(cnt =>
                if (cnt != 0){
                  Future.successful(Redirect(controllers.admin.routes.AgencyController.view(applicationId, agencyId)))
                }else{
                  Future(BadRequest(views.html.defaultproduct.edit("エラー", applicationId, agencyId, DefaultProductsForm.form.fill(defaultproduct), currentuser)))
                 }
              )
            }
        )
      }
    }
  }
  
  def delete(applicationId: Long, agencyId: Long, id: Long) = checkToken{
    AuthorizationAction(NormalUser).async { implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser = request.user
        defaultProductsDAO.disable(id, currentuser.id, agencyId).flatMap(cnt =>
          if (cnt != 0) Future.successful(Redirect(controllers.admin.routes.AgencyController.view(applicationId, agencyId)))
          else Future.successful(Status(500)(views.html.errors.error500internalerror("error")))
        )
      }
    }
  }
  

}