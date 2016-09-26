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

class BillingDestinationController @Inject()(addToken: CSRFAddToken, checkToken: CSRFCheck, cache: CacheApi, cached: Cached, val userAccountService: UserAccountServiceLike, billingDestinationsDao: BillingDestinationsDAO, billingAddressesDao: BillingAddressesDAO, contractsDao: ContractsDAO, clientsDao: ClientsDAO, agenciesDao: AgenciesDAO, applicationsDao: ApplicationsDAO, BillingDestinationsBillingAaddressesForm:BillingDestinationsBillingAaddressesForm, ContractBillingDestinationForm: ContractBillingDestinationForm, val messagesApi: MessagesApi) extends Controller with AuthActionBuilders with AuthElement with AuthConfigAdminImpl with I18nSupport  {
  
  val UserAccountSv = userAccountService
  
  val pageOffset = PageNation.defaultMaxPageNum
  
  
 
  def add(applicationId: Long, agencyId: Long) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
 
        Future(Ok(views.html.billingdestination.regist("", applicationId, agencyId, BillingDestinationsBillingAaddressesForm.form, currentuser)))
      }
    }
  }
  
  def create(applicationId: Long, agencyId: Long) = checkToken{
    AuthorizationAction(NormalUser).async { implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        BillingDestinationsBillingAaddressesForm.form.bindFromRequest.fold(
            formWithErrors => {
              Logger.debug(formWithErrors.toString())
              Future(BadRequest(views.html.billingdestination.regist("エラー", applicationId, agencyId, formWithErrors, currentuser)))
             },
            billingdestinationsbillingaaddresses => {
              billingDestinationsDao.create(billingdestinationsbillingaaddresses).flatMap(cnt =>
                  //if (cnt != 0) agenciesDao.all().map(agencies => Ok(views.html.agency.list("登録しました", agencies)))
                  if (cnt != 0) {
                    Future.successful(Redirect(controllers.admin.routes.AgencyController.view(applicationId, agencyId)))
                  }else{
                    Future(BadRequest(views.html.billingdestination.regist("エラー", applicationId, agencyId, BillingDestinationsBillingAaddressesForm.form.fill(billingdestinationsbillingaaddresses), currentuser)))
                  }
               )
            }
        )
      }
    }
  }
   

  def edit(applicationId: Long, agencyId: Long, billingDestinationId: Long) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        val billingOptions = for {
            billingdestination <- billingDestinationsDao.findById(billingDestinationId)
            billingaddress <- billingAddressesDao.findById(billingDestinationId)
        } yield (billingdestination, billingaddress)
        billingOptions.map { case (billingdestination, billingaddress) =>
          billingdestination match {
            case Some(billingdestination) => Ok(views.html.billingdestination.edit("GET", applicationId, agencyId, BillingDestinationsBillingAaddressesForm.form.fill(BillingDestinationsBillingAaddressesForm.createBillingDestinationsBillingAaddressesRow(billingdestination, billingaddress.getOrElse(null))), currentuser))
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
        BillingDestinationsBillingAaddressesForm.form.bindFromRequest.fold(
            formWithErrors => {
                  Future(BadRequest(views.html.billingdestination.edit("エラー", applicationId, agencyId, formWithErrors, currentuser)))
            },
            billingdestinationsbillingaaddresses => {
              billingDestinationsDao.update_mappinged(billingdestinationsbillingaaddresses).flatMap(cnt =>
                if (cnt != 0){
                  Future.successful(Redirect(controllers.admin.routes.AgencyController.view(applicationId, agencyId)))
                }else{
                  Future(BadRequest(views.html.billingdestination.edit("エラー", applicationId, agencyId, BillingDestinationsBillingAaddressesForm.form.fill(billingdestinationsbillingaaddresses), currentuser)))
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
        billingDestinationsDao.delete(id, agencyId).flatMap(cnt =>
          if (cnt != 0) Future.successful(Redirect(controllers.admin.routes.AgencyController.view(applicationId, agencyId)))
          else Future.successful(Status(500)(views.html.errors.error500internalerror("error")))
        )
      }
    }
  }
  
  def change(applicationId: Long, agencyId: Long, clientId: Long) = addToken{
    AuthorizationAction(NormalUser).async {implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false  || Await.result(clientsDao.checkExists(clientId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        val billingOptions = for {
            contractbillingdestination <- billingDestinationsDao.findContractBillingDestinationByClientId(clientId)
            billingdestinationlist <- billingDestinationsDao.findByAgencyIdWithAddress(agencyId)
            contract <- contractsDao.findByClientId(clientId)
        } yield (contractbillingdestination, billingdestinationlist, contract)
        billingOptions.map { case (contractbillingdestination, billingdestinationlist, contract) =>
          contractbillingdestination match {
            case Some(contractbillingdestination) => Ok(views.html.contractbillingdestination.change("GET", applicationId, agencyId, clientId, ContractBillingDestinationForm.form.fill(contractbillingdestination), billingdestinationlist.toList, currentuser))
            case _ => Ok(views.html.contractbillingdestination.change("GET", applicationId, agencyId, clientId, ContractBillingDestinationForm.form.fill(ContractBillingDestinationForm.createContractBillingDestinationRow(contract.map{row => row.id}.getOrElse(0))), billingdestinationlist.toList, currentuser))
          }
        }
      }
    }
  }

  def changeupdate(applicationId: Long, agencyId: Long, clientId: Long) = checkToken {
    AuthorizationAction(NormalUser).async { implicit request =>
      if(Await.result(applicationsDao.checkExists(applicationId), Duration.Inf) == false || Await.result(agenciesDao.checkExists(agencyId), Duration.Inf) == false  || Await.result(clientsDao.checkExists(clientId), Duration.Inf) == false){
        Future(Status(404)(views.html.errors.error404notfound("no found")))
      }else{
        val currentuser: User = request.user
        ContractBillingDestinationForm.form.bindFromRequest.fold(
            formWithErrors => {
              billingDestinationsDao.findByAgencyIdWithAddress(agencyId).map{ case billingdestinationlist =>
                  BadRequest(views.html.contractbillingdestination.change("エラー", applicationId, agencyId, clientId, formWithErrors, billingdestinationlist.toList, currentuser))
              }
            },
            contactbillingdestination => {
              billingDestinationsDao.update_mappinged(contactbillingdestination).flatMap(cnt =>
                if (cnt != 0){
                  Future.successful(Redirect(controllers.admin.routes.ClientController.view(applicationId, agencyId, clientId)))
                }else{
                  billingDestinationsDao.findByAgencyIdWithAddress(agencyId).map{ case billingdestinationlist =>
                    BadRequest(views.html.contractbillingdestination.change("エラー", applicationId, agencyId, clientId, ContractBillingDestinationForm.form.fill(contactbillingdestination), billingdestinationlist.toList, currentuser))
                  }
                }
              )
            }
        )
      }
    }
  }

}