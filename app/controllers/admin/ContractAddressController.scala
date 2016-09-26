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

class ContractAddressController @Inject()(addToken: CSRFAddToken, checkToken: CSRFCheck, cache: CacheApi, cached: Cached, val userAccountService: UserAccountServiceLike, contractAddressesDao: ContractAddressesDAO, clientsDao: ClientsDAO, agenciesDao: AgenciesDAO, applicationsDao: ApplicationsDAO, ContractAddressesForm:ContractAddressesForm, val messagesApi: MessagesApi) extends Controller with AuthActionBuilders with AuthElement with AuthConfigAdminImpl with I18nSupport  {
  
  val UserAccountSv = userAccountService
  
  val pageOffset = PageNation.defaultMaxPageNum
  
  
 
   

  def edit(applicationId: Long, agencyId: Long, clientId: Long) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false  || Await.result(clientsDao.checkExists(clientId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        contractAddressesDao.findByClientId(clientId).map { case contractaddress =>
          contractaddress match {
            case Some(contractaddress) => Ok(views.html.contractaddress.edit("GET", applicationId, agencyId, clientId, ContractAddressesForm.form.fill(contractaddress), currentuser))
            case _ => (Status(404)(views.html.errors.error404notfound("no found")))
          }
        }
      }
    }
  }

  def update(applicationId: Long, agencyId: Long, clientId: Long) = checkToken {
    AuthorizationAction(NormalUser).async { implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false  || Await.result(clientsDao.checkExists(clientId), Duration.Inf) == false){        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        ContractAddressesForm.form.bindFromRequest.fold(
            formWithErrors => {
                  Future(BadRequest(views.html.contractaddress.edit("エラー", applicationId, agencyId, clientId, formWithErrors, currentuser)))
            },
            contractaddress => {
              contractAddressesDao.update_mappinged(contractaddress).flatMap(cnt =>
                if (cnt != 0){
                  Future.successful(Redirect(controllers.admin.routes.ClientController.view(applicationId, agencyId, clientId)))
                }else{
                  Future(BadRequest(views.html.contractaddress.edit("エラー", applicationId, agencyId, clientId, ContractAddressesForm.form.fill(contractaddress), currentuser)))
                 }
              )
            }
        )
      }
    }
  }
  
  
}